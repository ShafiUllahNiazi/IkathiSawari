package com.example.shafi.ikathisawari.controllers.fragments.rider;


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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.AvailableDriversAdapter;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.models.AvailableDriverInfo;
import com.example.shafi.ikathisawari.models.DriverInfo;
import com.example.shafi.ikathisawari.models.DriverRoutInfo;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.example.shafi.ikathisawari.models.RiderRidePointsDriver;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderHome1 extends Fragment implements RoutingListener {

    private static final String TAG = "RiderHome1";

    private static final int PICKUP_Origin_PLACE_PICKER_REQUEST = 256;
    private static final int PICKUP_Destination_PLACE_PICKER_REQUEST = 671;


    private LatLng latLngCurrent, latLngDestination;
    Double originLat, originLong, destinationLat, destinationLong;


    private float radius = 1000;
    private DatabaseReference mDriverData;

    int myHour, myMinute, myYear, myMonth, myDay;
    boolean isDate, isTime;
    String dateDriver, timeDriver, seatsDriver, priceDriver;
    String dateRider, seatsRider;
    int traveledDistanceRider, traveledTimeRider;


    Location riderOriginAtRoad;
    Location riderDestinationAtRoad;


    Button saveRouteRider;

    EditText selectOriginRider, selectDestinationRider, selectDateRideRider, riderSeats;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AvailableDriversAdapter availableDriversAdapter;


    String current_Rider;
    RiderInfo riderInfo;
    ArrayList<AvailableDriverInfo> availableDriversList;

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putDouble("selectOriginRiderlat", latLngCurrent.latitude);
//        outState.putDouble("selectOriginRiderlng", latLngCurrent.longitude);
//        outState.putDouble("selectDestinationRiderlat", latLngDestination.latitude);
//        outState.putDouble("selectDestinationRiderlng", latLngDestination.longitude);
//
//        outState.putString("selectOriginRiderText", selectOriginRider.getText().toString());
//        outState.putString("selectDestinationRiderText", selectDestinationRider.getText().toString());
//
//        outState.putString("selectDateRideRider", selectDateRideRider.getText().toString());
//        outState.putString("riderSeats", riderSeats.getText().toString());
//
//        outState.putParcelableArrayList("availableDriversList", availableDriversList);
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if(savedInstanceState!=null) {
//            Double latCurrent = savedInstanceState.getDouble("selectOriginRiderlat");
//            Double lngCurrnet = savedInstanceState.getDouble("selectOriginRiderlng");
//            latLngCurrent = new LatLng(latCurrent, lngCurrnet);
//            Double latDestination = savedInstanceState.getDouble("selectDestinationRiderlat");
//            Double lngDestination = savedInstanceState.getDouble("selectDestinationRiderlng");
//            latLngDestination = new LatLng(latDestination, lngDestination);
//            selectOriginRider.setText(savedInstanceState.getString("selectOriginRiderText"));
//            selectDestinationRider.setText(savedInstanceState.getString("selectDestinationRiderText"));
//            selectDateRideRider.setText(savedInstanceState.getString("selectDateRideRider"));
//            riderSeats.setText(savedInstanceState.getString("riderSeats"));
//
//            availableDriversList = savedInstanceState.getParcelableArrayList("availableDriversList");
//        }
//    }

    public RiderHome1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_rider_home1, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAvailableDrivers1);
//        availableDriversList = new ArrayList<>();

        latLngCurrent = null;
        latLngDestination = null;

        isDate = false;
        isTime = false;


        selectOriginRider = view.findViewById(R.id.selectOriginRider);
        selectDestinationRider = view.findViewById(R.id.selectDestinationRider);
        selectDateRideRider = view.findViewById(R.id.selectDateRideRider);
        riderSeats = view.findViewById(R.id.riderSeats);
        saveRouteRider = view.findViewById(R.id.saveRouteR1);

        riderSeats = view.findViewById(R.id.riderSeats);

        selectOriginRider.setFocusable(false);
        selectOriginRider.setClickable(true);
        selectDestinationRider.setFocusable(false);
        selectDestinationRider.setClickable(true);
        selectDateRideRider.setFocusable(false);
        selectDateRideRider.setClickable(true);

        current_Rider = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReferenceRider = FirebaseDatabase.getInstance().getReference().child("users").child("Rider").child(current_Rider);
        databaseReferenceRider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                riderInfo = new RiderInfo();
                riderInfo = dataSnapshot.getValue(RiderInfo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

        saveRouteRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (latLngCurrent != null && latLngDestination != null) {

                    calculateDistanceAndTime(latLngCurrent, latLngDestination);


                } else {
                    Toast.makeText(getActivity(), "Locations empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        selectDateRideRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDTW();
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
                String h, min;
                myHour = hourOfDay;
                myMinute = minute;
                if (myHour < 10)
                    h = "0" + myHour;
                else
                    h = "" + myHour;
                if (myMinute < 10)
                    min = "0" + myMinute;
                else
                    min = "" + myMinute;
//                selectTimeRideRider.setText(h+":"+min);
                isTime = true;
                setDTW();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void setDTW() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getContext(), dayOfMonth+"", Toast.LENGTH_SHORT).show();
                String y, mon, d;
                myYear = year;
                myMonth = month + 1;
                myDay = dayOfMonth;
                y = myYear + "";
                if (myMonth < 10)
                    mon = "0" + myMonth;
                else
                    mon = "" + myMonth;
                if (myDay < 10)
                    d = "0" + myDay;
                else
                    d = "" + myDay;

//                dateTimeText.setText(myHour+":"+myMinute+" on "+myDay+" "+myMonth+":"+myYear);
                selectDateRideRider.setText(y + "-" + mon + "-" + d);

                isDate = true;

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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

    String rider_origin_name, rider_destination_name;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKUP_Origin_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getActivity(), data);
                selectOriginRider.setText(pickUpPlace.getName());
                rider_origin_name = pickUpPlace.getName().toString();
                latLngCurrent = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), pickUpPlace.getAddress() + "origin " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICKUP_Destination_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getActivity(), data);
                selectDestinationRider.setText(pickUpPlace.getName());
                rider_destination_name = pickUpPlace.getName().toString();
                latLngDestination = pickUpPlace.getLatLng();
                Toast.makeText(getActivity(), "Destination  " + pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            }
        }

    }

    String messagedriver, driver_origin_name, driver_destination_name, no_of_available_seats, vehicle_Model1;

    private void getNearByDrivers(final int traveledDistanceRider, final int traveledTimeRider, final String seatsRider) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Available Routs");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> myDrivers = (Map<String, Object>) dataSnapshot.getValue();

                for (String driver : myDrivers.keySet()) {
                    Log.d(TAG, "driver- " + myDrivers.get(driver));

                    Map<String, Object> dd = (Map<String, Object>) myDrivers.get(driver);

                    final DriverRoutInfo driverRoutInfo = new DriverRoutInfo();

                    driverRoutInfo.setRoutes((List<List<HashMap<String, String>>>) dd.get("routes"));
                    driver_origin_name = (String) dd.get("driver_origin_name");
                    driver_destination_name = (String) dd.get("driver_destination_name");
                    vehicle_Model1 = (String) dd.get("vehicle_Model1");
                    dateDriver = (String) dd.get("start_ride_date");
                    timeDriver = (String) dd.get("start_ride_time");
                    seatsDriver = (String) dd.get("no_of_seats");
                    priceDriver = (String) dd.get("price_per_km");
                    messagedriver = (String) dd.get("driver_message");
                    no_of_available_seats = (String) dd.get("no_of_available_seats");
                    Log.d(TAG, "onDataChange: " + dd.get("routes").toString());
                    final String availableDriver = fetchNearbyDriver(driver, driverRoutInfo, dateDriver, no_of_available_seats, priceDriver);
                    Log.d(TAG, "String " + availableDriver);

                    if (availableDriver != null) {
                        if (riderInfo != null) {


                            final int rideCharges = ((Integer.valueOf(priceDriver) * traveledDistanceRider) / 1000) * Integer.valueOf(seatsRider);
                            mDriverData = FirebaseDatabase.getInstance().getReference("users").child("Driver");
                            mDriverData.child(availableDriver).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DriverInfo driverInfo = dataSnapshot.getValue(DriverInfo.class);

                                    RiderRidePointsDriver riderOriginAtRoad1 = new RiderRidePointsDriver(riderOriginAtRoad.getLatitude(),riderOriginAtRoad.getLongitude());
                                    RiderRidePointsDriver riderDestinationAtRoad1 = new RiderRidePointsDriver(riderDestinationAtRoad.getLatitude(),riderDestinationAtRoad.getLongitude());

                                    AvailableDriverInfo availableDriverInfo = new AvailableDriverInfo(current_Rider,riderInfo,availableDriver, driverInfo, driverRoutInfo, dateDriver, timeDriver, seatsDriver, priceDriver, riderOriginAtRoad1, riderDestinationAtRoad1, dateRider, RiderHome1.this.seatsRider, traveledDistanceRider, traveledTimeRider, rideCharges, vehicle_Model1, driver_origin_name, driver_destination_name, messagedriver, rider_origin_name, rider_destination_name, no_of_available_seats);
                                    availableDriversList.add(availableDriverInfo);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
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

//
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        availableDriversAdapter = new AvailableDriversAdapter(getActivity(),getActivity().getSupportFragmentManager(), availableDriversList);
//
//        recyclerView.setAdapter(availableDriversAdapter);
//
//        availableDriversAdapter.notifyDataSetChanged();


        AvailableDrivers availableDrivers = new AvailableDrivers();
        Bundle b = new Bundle();
        b.putParcelableArrayList("availableDriversList", availableDriversList);
        b.putParcelable("from_position", latLngCurrent);
        b.putParcelable("to_position", latLngDestination);
        availableDrivers.setArguments(b);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rider_container, availableDrivers);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String fetchNearbyDriver(String driverKey, DriverRoutInfo driverRoutInfo, String date, String seats, String price) {

        Log.d("dddriver", driverKey);

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

                Log.d("diffff", "" + riderOriginToDriverOrigin);
                Log.d("diffff", "" + riderDestinationToDriverOrigin);


                if (riderOriginToDriverOrigin < riderDestinationToDriverOrigin) {

                    Log.d("mydebug", "if 6");
//                    Toast.makeText(getActivity(), "PathFound", Toast.LENGTH_SHORT).show();
                    Log.d("FoundLocation", "path Found");
                    Log.d("availableDriver", "path Found");
//                    availableDrivers.add(driverKey);

                    Log.d("ddddriver", driverKey);
//                    return driverKey;


                    ///////////////////////////////////////////


                    if (date.compareTo(selectDateRideRider.getText().toString()) <= 0) {
                        if (Integer.parseInt(seats) >= Integer.parseInt(riderSeats.getText().toString())) {


                            riderOriginAtRoad = new Location(originFoundLocation);
                            riderDestinationAtRoad = new Location(destinationFoundLocation);
                            return driverKey;
                        }
                    }


//                    return driverKey;
//                    Log.d("availableDriver", "Driver list added item " + availableDrivers.size());

                }


            }

        }


        return null;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.action_search) != null) {
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }


    }

    private void calculateDistanceAndTime(LatLng latLngCurrent, LatLng latLngDestination) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .key("AIzaSyBvR07aFM-1ddGVgt392lRnUge3weT6nUY")
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(latLngCurrent, latLngDestination)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

        if (e != null) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {

        traveledDistanceRider = route.get(0).getDistanceValue();
        traveledTimeRider = route.get(0).getDurationValue();
        saveRoute();


        Toast.makeText(getActivity(), "Route: " + (1) + ": distance - " + route.get(0).getDistanceValue() + ": duration - " + route.get(0).getDurationValue(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRoutingCancelled() {

    }

    private void saveRoute() {
        if (isDate) {
            dateRider = selectDateRideRider.getText().toString();
            seatsRider = riderSeats.getText().toString();

            if (!(seatsRider.equals(""))) {


                originLat = latLngCurrent.latitude;
                originLong = latLngCurrent.longitude;
                destinationLat = latLngDestination.latitude;
                destinationLong = latLngDestination.longitude;

                Log.d("locationssssss", latLngCurrent.latitude + " " + latLngCurrent.longitude + " Locations ..." + latLngDestination.latitude + " " + latLngDestination.longitude);

                availableDriversList = new ArrayList<>();
                if (traveledDistanceRider != 0 && traveledTimeRider != 0) {

                    getNearByDrivers(traveledDistanceRider, traveledTimeRider, seatsRider);
                }


//                    Toast.makeText(getActivity(), latLngCurrent.latitude+" "+latLngCurrent.longitude+" Locations ..."+ latLngDestination.latitude+" "+latLngDestination.longitude, Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getActivity(), "Provide available seats", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Select Date ", Toast.LENGTH_SHORT).show();
        }
    }


}
