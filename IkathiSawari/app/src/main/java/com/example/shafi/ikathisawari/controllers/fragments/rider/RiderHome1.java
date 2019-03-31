package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.models.AvailableDriverInfo;
import com.example.shafi.ikathisawari.models.DriverInfo;
import com.example.shafi.ikathisawari.models.DriverRoutInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderHome1 extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "RiderHome1";

    private static final int PICKUP_Origin_PLACE_PICKER_REQUEST = 256;
    private static final int PICKUP_Destination_PLACE_PICKER_REQUEST = 671;

    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;

    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted;


    Button selectOriginRider, selectDestinationRider,showRouteRider,saveRouteRider;
    TextView originLocationTextRider, destinationLocationTextRider;
    Spinner driverSpinner;

    private GoogleMap mMap;
    private LatLng latLngCurrent, latLngDestination;
    Double originLat, originLong, destinationLat, destinationLong;

    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    private DatabaseReference mRiderLocationPoints;
    ArrayList<AvailableDriverInfo> availableDriversList;
    private float radius = 100;
    private DatabaseReference mDriverData;


    public RiderHome1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rider_home1, container, false);
        mLocationPermissionGranted = false;
        latLngCurrent = null;
        latLngDestination = null;



//        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd hhmmss");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd hhmmss");
//        dateFormatter.setLenient(false);
//        Date today = new Date();
//        String s = dateFormatter.format(today);
//        Toast.makeText(getActivity(), "ss "+ s, Toast.LENGTH_SHORT).show();


        originLocationTextRider = view.findViewById(R.id.textselectOriginRider);
        destinationLocationTextRider = view.findViewById(R.id.textselectDestinationRider);
        selectOriginRider = view.findViewById(R.id.selectOriginRider);
        selectDestinationRider = view.findViewById(R.id.selectDestinationRider);
        showRouteRider = view.findViewById(R.id.showRouteR1);
        saveRouteRider = view.findViewById(R.id.saveRouteR1);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapDriverFragment1);


        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapRiderFragment1, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        selectOriginRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PICKUP_Origin_PLACE_PICKER_REQUEST);

                } catch (Exception e) {

                }
            }
        });

        selectDestinationRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PICKUP_Destination_PLACE_PICKER_REQUEST);

                } catch (Exception e) {

                }
            }
        });

        showRouteRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latLngCurrent != null && latLngDestination != null) {

                    new FetchURL(mMap,"showRoute",getActivity(), latLngCurrent, latLngDestination).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");
                    Toast.makeText(getActivity(), latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
                }



            }
        });

        saveRouteRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent!=null && latLngDestination!=null){
//                    sendData();
                    originLat = latLngCurrent.latitude;
                    originLong = latLngCurrent.longitude;
                    destinationLat = latLngDestination.latitude;
                    destinationLong = latLngDestination.longitude;

                    Log.d("locationssssss",latLngCurrent.latitude+" "+latLngCurrent.longitude+" Locations ..."+ latLngDestination.latitude+" "+latLngDestination.longitude);

                    availableDriversList = new ArrayList<>();
                    saveIntoFirebase();
                    getNearByDrivers();

                    Toast.makeText(getActivity(), latLngCurrent.latitude+" "+latLngCurrent.longitude+" Locations ..."+ latLngDestination.latitude+" "+latLngDestination.longitude, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKUP_Origin_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getActivity(), data);
                originLocationTextRider.setText(pickUpPlace.getAddress());
                latLngCurrent = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), pickUpPlace.getAddress() + "origin " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICKUP_Destination_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getActivity(), data);
                destinationLocationTextRider.setText(pickUpPlace.getAddress());
                latLngDestination = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), "Destination  " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }

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

                    final DriverRoutInfo driverRoutInfo = new DriverRoutInfo();

                    driverRoutInfo.setRoutes((List<List<HashMap<String, String>>>) dd.get("routes"));
                    Log.d(TAG, "onDataChange: "+dd.get("routes").toString());
                    final String availableDriver=fetchNearbyDriver(driver, driverRoutInfo);
                    Log.d(TAG,"String "+ availableDriver);

                    if(availableDriver != null){
                        mDriverData = FirebaseDatabase.getInstance().getReference("users").child("Driver");
                        mDriverData.child(availableDriver).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DriverInfo driverInfo = dataSnapshot.getValue(DriverInfo.class);
                                AvailableDriverInfo availableDriverInfo = new AvailableDriverInfo(availableDriver,driverInfo,driverRoutInfo);
                                availableDriversList.add(availableDriverInfo);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }



                }


//                Log.d(TAG,latLngCurrent)
//                Log.d(TAG, "Driver list size" + availableDriversList.size()+" "+availableDriversList.get(0)+" "+availableDriversList.get(1));
                Toast.makeText(getActivity(), "Driver list size" + availableDriversList.size(), Toast.LENGTH_SHORT).show();
                showAvailableDrivers(availableDriversList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showAvailableDrivers(ArrayList<AvailableDriverInfo> availableDriversList) {
        AvailableDrivers availableDrivers = new AvailableDrivers();
        Bundle b = new Bundle();
        b.putParcelableArrayList("availableDriversList",availableDriversList);
        b.putParcelable("from_position", latLngCurrent);
        b.putParcelable("to_position", latLngDestination);
        availableDrivers.setArguments(b);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rider_container, availableDrivers);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String fetchNearbyDriver(String driverKey, DriverRoutInfo driverRoutInfo) {

        Log.d("dddriver",driverKey );

        boolean originFound = false;
        boolean destinationFound = false;
        LatLng originFoundPoint = null;
        LatLng destinationFoundPoint = null;
        float originFoundPointDistance = radius;
        float destinationFoundPointDistance = radius;

        List<List<HashMap<String, String>>> result = driverRoutInfo.getRoutes();
        for (int i = 0; i < result.size(); i++) {

            Log.d("mydebug", "For 1");
            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {


                Log.d("mydebug", "For 2");
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
                    Log.d("mydebug", "if 1");

                    if (originDistance < originFoundPointDistance) {
                        Log.d("mydebug", "if 2");
                        originFound = true;
                        originFoundPoint = position;
                        originFoundPointDistance = originDistance;
                        Log.d("FoundLocation", "origin path Found");
//                        Toast.makeText(getActivity(), "Origin " + originFoundPoint.latitude + " " + originFoundPoint.longitude, Toast.LENGTH_SHORT).show();

                    }

                }
                if (destinationDistance < radius) {
                    Log.d("mydebug", "if 3");
                    if (destinationDistance < destinationFoundPointDistance) {
                        Log.d("mydebug", "if 4");
                        destinationFound = true;
                        destinationFoundPoint = position;
                        destinationFoundPointDistance = destinationDistance;
                        Log.d("FoundLocation", "destination path Found");

//                        Toast.makeText(getActivity(), "Destination " + destinationFoundPoint.latitude + " " + destinationFoundPoint.longitude, Toast.LENGTH_SHORT).show();
                    }

                }

            }


            if (originFound == true && destinationFound == true) {

                Log.d("mydebug", "if 5");
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

                Log.d("diffff",""+riderOriginToDriverOrigin);
                Log.d("diffff",""+riderDestinationToDriverOrigin);


                if (riderOriginToDriverOrigin < riderDestinationToDriverOrigin) {

                    Log.d("mydebug", "if 6");
//                    Toast.makeText(getActivity(), "PathFound", Toast.LENGTH_SHORT).show();
                    Log.d("FoundLocation", "path Found");
                    Log.d("availableDriver", "path Found");
//                    availableDrivers.add(driverKey);

                    Log.d("ddddriver",driverKey);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (isServicesOK()) {
                if (isLocationEnabled()) {
                    getLocationPermission();
                    if (mLocationPermissionGranted) {
                        Toast.makeText(getActivity(), "permission in on map ready", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                showSettingAlert();
            }



            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getDeviceLocation();
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
                        if (currentLocation != null) {
                            latLngCurrent = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                            Log.i(TAG, "onComplete: current location" + latLngCurrent.latitude + " " + latLngCurrent.longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, DEFAULT_ZOOM));
                            mMap.addMarker(new MarkerOptions().position(latLngCurrent).title("My Location"));
                        } else
                            Toast.makeText(getActivity(), "Unable to Fetch Current Location", Toast.LENGTH_LONG).show();
                    } else {
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
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
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
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
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: all permission granted");
            mLocationPermissionGranted = true;
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: called");

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: permission failed");
                    return;
                }
            }
            mLocationPermissionGranted = true;
            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        }
    }
}
