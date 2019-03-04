package com.example.shafi.ikathisawari.controllers;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.shafi.ikathisawari.models.DriverRoutInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAvailableDrivers {
    Double originLat, originLong, destinationLat, destinationLong;
    ArrayList<String> availableDrivers;
    private static final String TAG = "RiderDataCarrier";
    private float radius = 100;

    public FindAvailableDrivers(Double originLat, Double originLong, Double destinationLat, Double destinationLong) {
        this.originLat = originLat;
        this.originLong = originLong;
        this.destinationLat = destinationLat;
        this.destinationLong = destinationLong;
        availableDrivers = new ArrayList<>();
    }

    public ArrayList<String> getAvailableDrivers(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Available Routs");

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
//                        Toast.makeText(this, "Origin " + originFoundPoint.latitude + " " + originFoundPoint.longitude, Toast.LENGTH_SHORT).show();

                    }

                }
                if (destinationDistance < radius) {
                    if (destinationDistance < destinationFoundPointDistance) {
                        destinationFound = true;
                        destinationFoundPoint = position;
                        destinationFoundPointDistance = destinationDistance;
                        Log.d("FoundLocation", "destination path Found");

//                        Toast.makeText(this, "Destination " + destinationFoundPoint.latitude + " " + destinationFoundPoint.longitude, Toast.LENGTH_SHORT).show();
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

//                    Toast.makeText(this, "PathFound", Toast.LENGTH_SHORT).show();
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


}
