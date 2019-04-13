package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.services.UpdateDriverLocation;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverHome1 extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "DriverHome1";

    private static final int PICKUP_Origin_PLACE_PICKER_REQUEST = 256;
    private static final int PICKUP_Destination_PLACE_PICKER_REQUEST = 671;

    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;

    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted;


    Button selectOriginDriver, selectDestinationDriver,showRoute,saveRoute, selectTime;
    TextView originLocationTextDriver, destinationLocationTextDriver, dateTimeText;
    EditText driverSeats, driverPricePerKM;

    private GoogleMap mMap;
    private LatLng latLngCurrent, latLngDestination;
    Double originLat, originLong, destinationLat, destinationLong;

    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    int myHour, myMinute, myYear, myMonth, myDay;
    boolean isDate,isTime;


    public DriverHome1() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        Toast.makeText(getActivity(), "oncreatee", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_driver_home1, container, false);

        Intent intent = new Intent(getActivity(),UpdateDriverLocation.class);
        getActivity().stopService(intent);


        mLocationPermissionGranted = false;
        latLngCurrent = null;
        latLngDestination = null;

        isDate =false;
        isTime = false;

        originLocationTextDriver = view.findViewById(R.id.textselectOriginDriver);
        destinationLocationTextDriver = view.findViewById(R.id.textselectDestinationDriver);
        selectOriginDriver = view.findViewById(R.id.selectOriginDriver);
        selectDestinationDriver = view.findViewById(R.id.selectDestinationDriver);
        showRoute = view.findViewById(R.id.showRouteD1);
        saveRoute = view.findViewById(R.id.saveRouteD1);
        dateTimeText = view.findViewById(R.id.textDriverDateTime);
        selectTime = view.findViewById(R.id.selectDriverDateTime);
        driverSeats = view.findViewById(R.id.driverSeats);
        driverPricePerKM = view.findViewById(R.id.driverPricePerKM);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapDriverFragment1);


        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapDriverFragment1, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

//        driverSpinner = view.findViewById(R.id.spinnerDriverSeats);
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),)
//        driverSpinner.

        selectOriginDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PICKUP_Origin_PLACE_PICKER_REQUEST);

                } catch (Exception e) {

                }
            }
        });

        selectDestinationDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PICKUP_Destination_PLACE_PICKER_REQUEST);

                } catch (Exception e) {

                }
            }
        });

        showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();

                String seats = driverSeats.getText().toString();
                String price = driverPricePerKM.getText().toString();

                String dtStart = "2010-10-15T09:27";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                try {
                    Date date = format.parse(dtStart);
                    Toast.makeText(getActivity(), ""+date, Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
//                builder.setSmallIcon(R.mipmap.ic_launcher);
//                builder.setContentTitle("Firebase Push Notification");
//                builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
//                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(1, builder.build());
//                Intent intent = new Intent(DriverHome1.this, DriverDataCarrier.class);
//                if (latLngCurrent != null && latLngDestination != null) {
//
//                    new FetchURL(mMap,"showRoute",getActivity(), latLngCurrent, latLngDestination).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");
//                    Toast.makeText(getActivity(), latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
//                }



            }
        });

        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent != null && latLngDestination != null) {
                    if(isTime & isDate){
                        String timeAndDate =dateTimeText.getText().toString();
                        String seats = driverSeats.getText().toString();
                        String price = driverPricePerKM.getText().toString();
//                        Toast.makeText(getActivity(), seats +" "+ price, Toast.LENGTH_SHORT).show();
                        if(!(seats.equals(""))){
                            if(!(price.equals(""))){
                                new FetchURL(mMap,"saveRoute",getActivity(),latLngCurrent, latLngDestination,timeAndDate,seats,price).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");

                                Log.d("dddddddddddd",seats+" "+price);

                            }else {
                                Toast.makeText(getActivity(), "Provide the price for one Km", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getActivity(), "Provide available seats", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getActivity(), "Select Date and Time", Toast.LENGTH_SHORT).show();
                    }

//                    new FetchURL(mMap,"saveRoute",getActivity(),latLngCurrent, latLngDestination).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");
//                    Toast.makeText(getActivity(), latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSchedule();
//                setDTW();


//                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });


        return view;
    }


    private void setSchedule() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                Toast.makeText(getContext(), hourOfDay+""+minute, Toast.LENGTH_SHORT).show();
                myHour = hourOfDay;
                myMinute = minute;
                isTime = true;
                setDTW();
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    private void setDTW() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getContext(), dayOfMonth+"", Toast.LENGTH_SHORT).show();
                String y,mon,d,h,min;
                myYear = year;
                myMonth = month+1;
                myDay = dayOfMonth;
                y = myYear+"";
                if(myMonth<10)
                    mon = "0"+myMonth;
                else
                    mon = ""+myMonth;
                if(myDay<10)
                    d = "0"+myDay;
                else
                    d = ""+myDay;
                if(myHour<10)
                    h = "0"+myHour;
                else
                    h = ""+myHour;
                if(myMinute<10)
                    min = "0"+myMinute;
                else
                    min = ""+myMinute;
//                dateTimeText.setText(myHour+":"+myMinute+" on "+myDay+" "+myMonth+":"+myYear);
                dateTimeText.setText(y+"-"+mon+"-"+d+"T"+h+":"+min);

                isDate = true;

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

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
                originLocationTextDriver.setText(pickUpPlace.getAddress());
                latLngCurrent = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), pickUpPlace.getAddress() + "origin " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICKUP_Destination_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getActivity(), data);
                destinationLocationTextDriver.setText(pickUpPlace.getAddress());
                latLngDestination = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), "Destination  " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initViews(View view) {
        originLocationTextDriver = view.findViewById(R.id.textselectOriginDriver);
        destinationLocationTextDriver = view.findViewById(R.id.textselectDestinationDriver);
        selectOriginDriver = view.findViewById(R.id.selectOriginDriver);
        selectDestinationDriver = view.findViewById(R.id.selectDestinationDriver);
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


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
////        MenuInflater menuInflater = getActivity().getMenuInflater();
//
//        super.onCreateOptionsMenu(menu, inflater);
////        menu.clear();
//        inflater.inflate(R.menu.toolbar_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        searchItem.setVisible(false);
//
//
//
//    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        if(menu.findItem(R.id.action_search) !=null ){
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }


    }
}
