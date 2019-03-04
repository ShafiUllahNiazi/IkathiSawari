package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.DriverRoutInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RiderHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RiderHome extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    private static final float DEFAULT_ZOOM = 15f;

    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final String TAG = "RiderHome";

    private PlaceAutocompleteFragment autocompleteFragment;
    private PlaceAutocompleteFragment autocompleteFragment_destination;
    private GoogleMap mMap;
    private LatLng latLngCurrent, latLngDestination;

    Button saveRouteFragment ;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private DatabaseReference mRiderLocationPoints;
    ArrayList<String> availableDriversList;
    private float radius = 100;
    Double originLat, originLong, destinationLat, destinationLong;


    public RiderHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RiderHome.
     */
    // TODO: Rename and change types and number of parameters
    public static RiderHome newInstance(String param1, String param2) {
        RiderHome fragment = new RiderHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isServicesOK()){
            if(isLocationEnabled()){
                getLocationPermission();
                if (mLocationPermissionGranted){
                    navigateToMap();
                }
            }
        }else {
            showSettingAlert();
        }


        saveRouteFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent!=null && latLngDestination!=null){
//                    sendData();
                    originLat = latLngCurrent.latitude;
                    originLong = latLngCurrent.longitude;
                    destinationLat = latLngDestination.latitude;
                    destinationLong = latLngDestination.longitude;

                    saveIntoFirebase();
                    getNearByDrivers();
                    Toast.makeText(getActivity(), latLngCurrent.latitude+" "+latLngCurrent.longitude+" Locations ..."+ latLngDestination.latitude+" "+latLngDestination.longitude, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getNearByDrivers() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Available Routs");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String,Object> myDrivers = (Map<String, Object>) dataSnapshot.getValue();

                for (String driver: myDrivers.keySet()){
                    Log.d(TAG, "driver- " + myDrivers.get(driver) );

                    Map<String,Object> dd = (Map<String, Object>) myDrivers.get(driver);

                    DriverRoutInfo driverRoutInfo = new DriverRoutInfo();

                    driverRoutInfo.setRoutes((List<List<HashMap<String, String>>>) dd.get("routes"));
                    Log.d(TAG, "onDataChange: "+dd.get("routes").toString());
                    String availableDriver=fetchNearbyDriver(driver, driverRoutInfo);


                    availableDriversList.add(availableDriver);
                }


//                Log.d(TAG,latLngCurrent)
                Log.d(TAG, "Driver list size" + availableDriversList.size()+" "+availableDriversList.get(0)+" "+availableDriversList.get(1));
                Toast.makeText(getActivity(), "Driver list size" + availableDriversList.size(), Toast.LENGTH_SHORT).show();
                showAvailableDrivers(availableDriversList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showAvailableDrivers(ArrayList<String> availableDriversList) {
        AvailableDrivers availableDrivers = new AvailableDrivers();
        Bundle b = new Bundle();
        b.putStringArrayList("availableDriversList",availableDriversList);
        availableDrivers.setArguments(b);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rider_container, availableDrivers);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String fetchNearbyDriver(String driverKey, DriverRoutInfo driverRoutInfo) {

        boolean originFound = false;
        boolean destinationFound = false;
        LatLng originFoundPoint = null;
        LatLng destinationFoundPoint = null;
        float originFoundPointDistance = radius;
        float destinationFoundPointDistance = radius;

        List<List<HashMap<String, String>>> result = driverRoutInfo.getRoutes();
        for (int i = 0; i < result.size(); i++) {

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {


                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                Location riderOriginLocation = new Location(LocationManager.GPS_PROVIDER);
                riderOriginLocation.setLatitude(originLat);
                riderOriginLocation.setLongitude(originLong);

                Location riderDestinationLocation = new Location(LocationManager.GPS_PROVIDER);
                riderDestinationLocation.setLatitude(destinationLat);
                riderDestinationLocation.setLongitude(destinationLong);

                Location positionOnPath = new Location(LocationManager.GPS_PROVIDER);
                positionOnPath.setLatitude(position.latitude);
                positionOnPath.setLongitude(position.longitude);
                float originDistance = riderOriginLocation.distanceTo(positionOnPath);
                float destinationDistance = riderDestinationLocation.distanceTo(positionOnPath);

//                Toast.makeText(this, "dis "+rr, Toast.LENGTH_SHORT).show();
                Log.d("myLocationss", "" + originDistance + " " + destinationDistance);
                if (originDistance < radius) {
                    if (originDistance < originFoundPointDistance) {
                        originFound = true;
                        originFoundPoint = position;
                        originFoundPointDistance = originDistance;
                        Log.d("FoundLocation", "origin path Found");
//                        Toast.makeText(getActivity(), "Origin " + originFoundPoint.latitude + " " + originFoundPoint.longitude, Toast.LENGTH_SHORT).show();

                    }

                }
                if (destinationDistance < radius) {
                    if (destinationDistance < destinationFoundPointDistance) {
                        destinationFound = true;
                        destinationFoundPoint = position;
                        destinationFoundPointDistance = destinationDistance;
                        Log.d("FoundLocation", "destination path Found");

//                        Toast.makeText(getActivity(), "Destination " + destinationFoundPoint.latitude + " " + destinationFoundPoint.longitude, Toast.LENGTH_SHORT).show();
                    }

                }

            }


            if (originFound == true && destinationFound == true) {
                Location originFoundLocation = new Location(LocationManager.GPS_PROVIDER);
                originFoundLocation.setLatitude(originFoundPoint.latitude);
                originFoundLocation.setLongitude(originFoundPoint.longitude);

                Location destinationFoundLocation = new Location(LocationManager.GPS_PROVIDER);
                destinationFoundLocation.setLatitude(destinationFoundPoint.latitude);
                destinationFoundLocation.setLongitude(destinationFoundPoint.longitude);

                //driver Origin

                HashMap<String, String> driverOrigin = path.get(0);
                double lat1 = Double.parseDouble(driverOrigin.get("lat"));
                double lng1 = Double.parseDouble(driverOrigin.get("lng"));
//                LatLng originPosition = new LatLng(lat, lng);

                Location driverOriginLocation = new Location(LocationManager.GPS_PROVIDER);
                driverOriginLocation.setLatitude(lat1);
                driverOriginLocation.setLongitude(lng1);


                float riderOriginToDriverOrigin = originFoundLocation.distanceTo(driverOriginLocation);
                float riderDestinationToDriverOrigin = destinationFoundLocation.distanceTo(driverOriginLocation);


                if (riderOriginToDriverOrigin < riderDestinationToDriverOrigin) {

//                    Toast.makeText(getActivity(), "PathFound", Toast.LENGTH_SHORT).show();
                    Log.d("FoundLocation", "path Found");
                    Log.d("availableDriver", "path Found");
//                    availableDrivers.add(driverKey);
                    return driverKey;
//                    Log.d("availableDriver", "Driver list added item " + availableDrivers.size());

                }


            }

        }


        return null;
    }

    private void saveIntoFirebase() {

        mRiderLocationPoints = FirebaseDatabase.getInstance().getReference("Rider Routs");
        String rider_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map locationData = new HashMap();
        locationData.put("originLat", latLngCurrent.latitude);
        locationData.put("originLong", latLngCurrent.longitude);
        locationData.put("destinationLat", latLngDestination.latitude);
        locationData.put("destinationLong", latLngDestination.longitude);
        mRiderLocationPoints.child(rider_id).setValue(locationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getActivity(), "data has been added.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Unable to add data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_rider_home, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
//        View view =  inflater.inflate(R.layout.fragment_rider_home, container, false);
        availableDriversList = new ArrayList<>();

        saveRouteFragment = view.findViewById(R.id.saveRouteFragment);

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.mapRiderFragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapRiderFragment);

        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapRiderFragment,mapFragment).commit();
        }

        mapFragment.getMapAsync(this);





        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_originRider_Fragment);
        autocompleteFragment.setHint("Select your origin");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();
                latLngCurrent = place.getLatLng();
                Log.i(TAG, "onPlaceSelected: current location" + latLngCurrent.latitude + " " + latLngCurrent.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLngCurrent));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
            }

            @Override
            public void onError(Status status) {

            }
        });

        autocompleteFragment_destination = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_destinationRider_Fragment);
        autocompleteFragment_destination.setHint("Select your destination");
        autocompleteFragment_destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latLngDestination = place.getLatLng();
                Log.i(TAG, "onPlaceSelected: destination location" + latLngDestination.latitude + " " + latLngDestination.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLngDestination));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
            }

            @Override
            public void onError(Status status) {

            }
        });




        return view;
    }

    private boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOGE_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showSettingAlert();
            return false;
        }
        return true;
    }

    private void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("location?");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void getLocationPermission() {
        Log.i(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "getLocationPermission: all permission granted");
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: called");

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED ){
                    Log.i(TAG, "onRequestPermissionsResult: permission failed");
                    return;
                }
            }
            mLocationPermissionGranted = true;
            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        }
    }

    private void navigateToMap() {
        //navigate to map fragment
//        Toast.makeText(getActivity(), "map called", Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getDeviceLocation();
        //onMapClicked();
//        onMarkerClicked();
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        if (currentLocation != null){
                            latLngCurrent = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            Log.i(TAG, "onComplete: current location" + latLngCurrent.latitude + " " + latLngCurrent.longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, DEFAULT_ZOOM));
                            mMap.addMarker(new MarkerOptions().position(latLngCurrent).title("My Location"));
                        }else
                            Toast.makeText(getActivity(), "Unable to Fetch Current Location", Toast.LENGTH_LONG).show();
                    } else {
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
