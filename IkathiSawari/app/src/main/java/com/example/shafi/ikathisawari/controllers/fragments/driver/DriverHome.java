package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.directionhelpers.PointsParser;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverHome extends Fragment implements OnMapReadyCallback {

    private static View view;
    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    private static final float DEFAULT_ZOOM = 15f;

    private static final String TAG = "DriverHome";

    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted;


    Button saveRouteDriverFragment;

    private PlaceAutocompleteFragment autocompleteFragment;
    private PlaceAutocompleteFragment autocompleteFragment_destination;
    private LatLng latLngCurrent, latLngDestination;
    Double originLat, originLong, destinationLat, destinationLong;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;
    private Polyline currentPolyline;

    public DriverHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "on create");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_driver_home, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        mLocationPermissionGranted = false;
        latLngCurrent = null;
        latLngDestination = null;

        saveRouteDriverFragment = view.findViewById(R.id.saveRouteDriverFragment);
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.mapRiderFragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapDriverFragment);


        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapDriverFragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_originDriver_Fragment);
        autocompleteFragment.setText(null);
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

        autocompleteFragment_destination = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_destinationDriver_Fragment);
        autocompleteFragment_destination.setText(null);
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

    @Override
    public void onResume() {
        super.onResume();



        saveRouteDriverFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent != null && latLngDestination != null) {
//                    sendData();
                    originLat = latLngCurrent.latitude;
                    originLong = latLngCurrent.longitude;
                    destinationLat = latLngDestination.latitude;
                    destinationLong = latLngDestination.longitude;

                    Log.d("locationssssss", latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude);

                    showRouteOnMap();
//                    saveIntoFirebase();
//                    getNearByDrivers();
                    Toast.makeText(getActivity(), latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    Context context;
    private void showRouteOnMap() {
//        mListener.onRequirePoints(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");

//        new FetchURL(mMap,currentPolyline).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");
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

    private void navigateToMap() {
        //navigate to map fragment
//        Toast.makeText(getActivity(), "map called", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "map ready");

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Log.d(TAG, "map ready if");
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
        Log.d(TAG, "map ready if out ");
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
                            Log.i(TAG, "onComplete: current location" + latLngCurrent.latitude + " " + latLngCurrent.longitude);
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


//    public interface OnRequirePoints{
//        void onRequirePoints(String str1, String str2);
//    }
//    public OnRequirePoints mListener;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mListener = (OnRequirePoints) context;
//        this.context = context;
//    }

//    @Override
//    public void onTaskDone(Object... values) {
//        if (currentPolyline != null)
//            currentPolyline.remove();
//        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
//    }
}
