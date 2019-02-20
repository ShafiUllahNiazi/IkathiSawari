package com.example.shafi.ikathisawari;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.directionhelpers.PointsParser;
import com.example.shafi.ikathisawari.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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

import java.util.HashMap;
import java.util.List;

public class DriverHomeMap extends FragmentActivity implements /*RoutesObserver,*/ OnMapReadyCallback, TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {



    private static final String TAG = "DriverHomeMap";
    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private PlaceAutocompleteFragment autocompleteFragment;
    private PlaceAutocompleteFragment autocompleteFragment_destination;
    private LocationManager locationManager;
    private Location location;
    private LatLng latLngCurrent, latLngDestination;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button saveRoute, showRoute;

    private Polyline currentPolyline;


    GoogleApiClient mGoogleApiClient;
    Location mLoaLocation;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_home_map);

//        PointsParser pointsParser=new PointsParser(this, "driving");
//        pointsParser.confirm(this);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
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

        autocompleteFragment_destination = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_destination);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveRoute = findViewById(R.id.sendData);
        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent!=null && latLngDestination!=null){
                    sendData();
                }else {
                    Toast.makeText(DriverHomeMap.this, "Locations empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        showRoute = findViewById(R.id.showRoute);
        showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent!=null && latLngDestination!=null){
                    showRouteOnMap();
                }else {
                    Toast.makeText(DriverHomeMap.this, "Locations empty...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showRouteOnMap() {
        new FetchURL(DriverHomeMap.this).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");
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
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getDeviceLocation();
        //onMapClicked();
        onMarkerClicked();
    }

    public void onMarkerClicked() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                latLngDestination = marker.getPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomeMap.this);
                builder.setTitle("Confirm");
                builder.setMessage("Confirm Location?");
                builder.setCancelable(false);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });
    }


    @SuppressLint("MissingPermission")
    public void getLocation() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i(TAG, "getLocation: latlng current" + location.getLatitude() + "  " + location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 13));
            mMap.addMarker(new MarkerOptions().position(latLngCurrent));
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
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
                            Toast.makeText(DriverHomeMap.this, "Unable to Fetch Current Location", Toast.LENGTH_LONG).show();
                    } else {
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    public void onMapClicked() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                latLngDestination = latLng;
                mMap.addMarker(new MarkerOptions()
                        .position(latLngDestination));
                float[] distance = new float[1];
//                Location.distanceBetween(latLngCurrent.latitude, latLngCurrent.longitude, latLngDestination.latitude, latLngDestination.longitude, distance);
            }
        });
    }

    public void sendData() {
        Intent intent = new Intent(DriverHomeMap.this, DriverDataCarrier.class);
        Log.i(TAG, "sendData: current" + latLngCurrent.latitude + "  " + latLngCurrent.longitude );
        Log.i(TAG, "sendData: destination " + latLngDestination.latitude + "  " + latLngDestination.longitude);
        intent.putExtra("currentLat", latLngCurrent.latitude);
        intent.putExtra("currentLong", latLngCurrent.longitude);
        intent.putExtra("destinationLat", latLngDestination.latitude);
        intent.putExtra("destinationLong", latLngDestination.longitude);

        //setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    public void theTaskIsDone(List<List<HashMap<String, String>>> routes) {
//
//        Toast.makeText(this, "dsadasd", Toast.LENGTH_SHORT).show();
//        Log.d("Yousaf","absashifhsefhsa");
//
//    }
}


