package com.example.shafi.ikathisawari;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shafi.ikathisawari.models.DriverRoutInfo;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderDataCarrier extends AppCompatActivity {
    private static final String TAG = "RiderDataCarrier";
    Intent intent;
    Double originLat, originLong, destinationLat, destinationLong;
    private DatabaseReference mRiderAvailableRout;
    Button goBackHomeRider, getDriver;

    private float radius = 100;
    private boolean driverFound = false;


    ArrayList<String> availableDrivers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_data_carrier);
        goBackHomeRider = findViewById(R.id.goBackHomeRider);
        getDriver = findViewById(R.id.getDriver);

        availableDrivers = new ArrayList<>();

        intent = getIntent();

        originLat = intent.getDoubleExtra("originLat", 0);
        originLong = intent.getDoubleExtra("originLong", 0);
        destinationLat = intent.getDoubleExtra("destinationLat", 1);
        destinationLong = intent.getDoubleExtra("destinationLong", 1);
        Log.i("receiveData", "receiveData: current " + originLat + "  " + originLong);
        Log.i("receiveData", "receiveData: destination " + destinationLat + "  " + destinationLong);

        saveIntoFirebase();

    }


    @Override
    protected void onResume() {
        super.onResume();
        goBackHomeRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderDataCarrier.this, Rider_Home.class));
            }
        });

        getDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String>drivers = new ArrayList<>();
                drivers = getNearbyDriver();
                Log.d(TAG, "Driver list sizeee" + drivers.size());
            }
        });

    }

    private ArrayList<String> getNearbyDriver() {
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
                    String availableDriver=fetchNearbyPoint(driver, driverRoutInfo);


                    availableDrivers.add(availableDriver);
                }


                Log.d(TAG, "Driver list size" + availableDrivers.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return availableDrivers;


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.child("").child("").;
//                for (final DataSnapshot child : dataSnapshot.getChildren()) {
//                    databaseReference.child(child.getKey()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            DriverRoutInfo driverRoutInfo = dataSnapshot.getValue(DriverRoutInfo.class);
//                            Log.d("myFetchRoute ", driverRoutInfo.toString());
//                            Log.d("myFetchRoute111 ", driverRoutInfo.getRoutes().toString());
////                            Toast.makeText(RiderDataCarrier.this, ""+child.getKey(), Toast.LENGTH_SHORT).show();
//
//                            String availableDriver=fetchNearbyPoint(child.getKey(), driverRoutInfo);
//
//                            Log.d("availableDriver ","returned "+availableDriver);
//                            availableDrivers.add(availableDriver);
//
//                            Log.d("TESTER", "onDataChange: size - "+availableDriver.length());
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
////                    Toast.makeText(RiderDataCarrier.this, ""+child.getKey().toString(), Toast.LENGTH_SHORT).show();
//                }
//
//
//
//                Log.d("availableDriver", "Driver list size" + availableDrivers.size());
//
//                for (int i = 0; i < availableDrivers.size(); i++) {
//
//                    Log.d("availableDriver", "Driver " + i + " " + availableDrivers.get(i));
//                    Toast.makeText(RiderDataCarrier.this, "Driver " + i + " " + availableDrivers.get(i), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    private String fetchNearbyPoint(String driverKey, DriverRoutInfo driverRoutInfo) {

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
                        Toast.makeText(this, "Origin " + originFoundPoint.latitude + " " + originFoundPoint.longitude, Toast.LENGTH_SHORT).show();

                    }

                }
                if (destinationDistance < radius) {
                    if (destinationDistance < destinationFoundPointDistance) {
                        destinationFound = true;
                        destinationFoundPoint = position;
                        destinationFoundPointDistance = destinationDistance;
                        Log.d("FoundLocation", "destination path Found");

                        Toast.makeText(this, "Destination " + destinationFoundPoint.latitude + " " + destinationFoundPoint.longitude, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(this, "PathFound", Toast.LENGTH_SHORT).show();
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

        mRiderAvailableRout = FirebaseDatabase.getInstance().getReference("Rider Routs");
        String rider_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map locationData = new HashMap();
        locationData.put("originLat", originLat);
        locationData.put("originLong", originLong);
        locationData.put("destinationLat", destinationLat);
        locationData.put("destinationLong", destinationLong);
        mRiderAvailableRout.child(rider_id).setValue(locationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RiderDataCarrier.this, "data has been added.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RiderDataCarrier.this, "Unable to add data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.sign_out) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));

            return true;
        }

        return true;

    }

}
