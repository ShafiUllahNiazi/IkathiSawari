package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.ShowRoute;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.models.FetchRouteData;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverHome1 extends Fragment  {

    private static final String TAG = "DriverHome1";

    private static final int PICKUP_Origin_PLACE_PICKER_REQUEST = 256;
    private static final int PICKUP_Destination_PLACE_PICKER_REQUEST = 671;

    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;

    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted;


    Button saveRoute,showRouteD1;

    String pickUpPlaceName, destinationPlaceName;

    EditText selectOriginDriver, selectDestinationDriver,carModel,selectDateRideDriver, selectTimeRideDriver,driverSeats, driverPricePerKM,driver_message;

    private GoogleMap mMap;
    private LatLng latLngCurrent, latLngDestination;
    Double originLat, originLong, destinationLat, destinationLong;

    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    int myHour, myMinute, myYear, myMonth, myDay;
    boolean isDate,isTime;
    private ProgressDialog progressDialog;


    public DriverHome1() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Toast.makeText(getActivity(), "oncreatee", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_driver_home1, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);




        mLocationPermissionGranted = false;
        latLngCurrent = null;
        latLngDestination = null;

        isDate =false;
        isTime = false;


        selectOriginDriver = view.findViewById(R.id.selectOriginDriver);
        selectDestinationDriver = view.findViewById(R.id.selectDestinationDriver);
        selectDateRideDriver = view.findViewById(R.id.selectDateRideDriver);
        selectTimeRideDriver = view.findViewById(R.id.selectTimeRideDriver);
        driverSeats = view.findViewById(R.id.driverSeats);
        driverPricePerKM = view.findViewById(R.id.driverPricePerKM);
        driver_message = view.findViewById(R.id.driver_message);
        carModel = view.findViewById(R.id.carModel);
        saveRoute = view.findViewById(R.id.saveRouteD1);
        showRouteD1 = view.findViewById(R.id.showRouteD1);
        selectOriginDriver.setFocusable(false);
        selectOriginDriver.setClickable(true);
        selectDestinationDriver.setFocusable(false);
        selectDestinationDriver.setClickable(true);

        selectDateRideDriver.setFocusable(false);
        selectDateRideDriver.setClickable(true);

        selectTimeRideDriver.setFocusable(false);
        selectTimeRideDriver.setClickable(true);


        showRouteD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngCurrent != null) {
                    if (latLngDestination != null) {
                        seeRoute();
                    }else {
                        Toast.makeText(getActivity(), "Select Destination", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Select Origin", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
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

        selectDateRideDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDTW();
            }
        });

        selectTimeRideDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSchedule();
            }
        });

        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latLngCurrent != null) {


                if (latLngDestination != null) {

                    String carModel1 = carModel.getText().toString();
                    if(!(carModel1.equals(""))){
                        if(isDate){
                            String date = selectDateRideDriver.getText().toString();

                            if(isTime){
                                String time =selectTimeRideDriver.getText().toString();
                                String seats = driverSeats.getText().toString();
                                String price = driverPricePerKM.getText().toString();
//                        Toast.makeText(getActivity(), seats +" "+ price, Toast.LENGTH_SHORT).show();
                                if(!(seats.equals(""))){
                                    if(!(price.equals(""))){
                                        String driver_message1 = driver_message.getText().toString();
//                                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                                        progressDialog.setMessage("Offering Ride");
                                        progressDialog.show();
                                        new FetchURL(new FetchRouteData(mMap,"saveRoute",getActivity(),latLngCurrent, latLngDestination,pickUpPlaceName,destinationPlaceName,carModel1,date,time,seats,price,driver_message1,progressDialog)).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");

                                        Log.d("dddddddddddd",seats+" "+price);

                                    }else {
                                        Toast.makeText(getActivity(), "Provide the price for one Km", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(getActivity(), "Provide available seats", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(getActivity(), "Select  Time", Toast.LENGTH_SHORT).show();
                            }}else {
                            Toast.makeText(getActivity(), "Select  Date", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Kindly provide the vehicle model", Toast.LENGTH_SHORT).show();
                    }



//                    new FetchURL(mMap,"saveRoute",getActivity(),latLngCurrent, latLngDestination).execute(getUrl(latLngCurrent, latLngDestination, "driving"), "driving");
//                    Toast.makeText(getActivity(), latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Destination  empty...", Toast.LENGTH_SHORT).show();
                }

            }else {
                    Toast.makeText(getActivity(), "Origin empty...", Toast.LENGTH_SHORT).show();
                }

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
                String h,min;
                myHour = hourOfDay;
                myMinute = minute;
                isTime = true;
                if(myHour<10)
                    h = "0"+myHour;
                else
                    h = ""+myHour;
                if(myMinute<10)
                    min = "0"+myMinute;
                else
                    min = ""+myMinute;
                selectTimeRideDriver.setText(h+":"+min);
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
                String y,mon,d;
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

//                dateTimeText.setText(myHour+":"+myMinute+" on "+myDay+" "+myMonth+":"+myYear);

                selectDateRideDriver.setText(y+"-"+mon+"-"+d);

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
                pickUpPlaceName = pickUpPlace.getName().toString();
                selectOriginDriver.setText(pickUpPlaceName);
                latLngCurrent = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), pickUpPlace.getAddress() + "origin " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICKUP_Destination_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getActivity(), data);
                destinationPlaceName = pickUpPlace.getName().toString();
                selectDestinationDriver.setText(destinationPlaceName);
                latLngDestination = pickUpPlace.getLatLng();

//                String gg = getRegionName(latLngDestination.latitude,latLngDestination.longitude);
//                selectDestinationDriver.setText(gg);
                Toast.makeText(getActivity(), "Destination  " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private  String getRegionName(double lati, double longi) {
        String regioName = "";
        Geocoder gcd = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(lati, longi, 1);
            if (addresses.size() > 0) {
//                regioName = addresses.get(0).getLocality();
                regioName = addresses.get(0)+" kk";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regioName;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        if(menu.findItem(R.id.action_search) !=null ){
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }


    }

    private void seeRoute(){
        ShowRoute showRoute = new ShowRoute();
        Bundle b = new Bundle();

        b.putParcelable("from_position", latLngCurrent);
        b.putParcelable("to_position", latLngDestination);
        showRoute.setArguments(b);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_container, showRoute);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
