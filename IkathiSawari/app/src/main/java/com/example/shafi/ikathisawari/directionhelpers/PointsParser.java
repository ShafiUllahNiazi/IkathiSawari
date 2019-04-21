package com.example.shafi.ikathisawari.directionhelpers;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.shafi.ikathisawari.DriverHomeMap;
import com.example.shafi.ikathisawari.RoutesObserver;
import com.example.shafi.ikathisawari.models.DriverRoutInfo;
import com.example.shafi.ikathisawari.models.FetchRouteData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vishal on 10/20/2018.
 */

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//    TaskLoadedCallback taskCallback;
    String directionMode = "driving";
//    Context actContext ;
    private GoogleMap mMap;
    private Polyline currentPolyline;
    String clickButton;
    Context context;
    private LatLng latLngCurrent, latLngDestination;
    String date,time, seats,price;
    String pickUpPlaceName;
    String destinationPlaceName;
    String carModel1;
    String driver_message1;

    public PointsParser(FetchRouteData fetchRouteData, String directionMode) {
        this.directionMode = directionMode;
        this.mMap = fetchRouteData.getmMap();
        this.clickButton = fetchRouteData.getClickButton();
        this.context =fetchRouteData.getContext();
        this.latLngCurrent = fetchRouteData.getLatLngCurrent();
        this.latLngDestination = fetchRouteData.getLatLngDestination();
        this.date = fetchRouteData.getDate();
        this.time = fetchRouteData.getTime();
        this.seats = fetchRouteData.getSeats();
        this.price = fetchRouteData.getPrice();
        this.carModel1 = fetchRouteData.getCarModel1();
        this.driver_message1 = fetchRouteData.getDriver_message1();
        this.pickUpPlaceName =fetchRouteData.getPickUpPlaceName();
        this.destinationPlaceName = fetchRouteData.getDestinationPlaceName();
    }

    public interface TaskLoadedCallback {
        void onTaskDone(Object... values);
    }

//    public PointsParser(GoogleMap map, String clickButton, Context context, LatLng latLngCurrent,
//                        LatLng latLngDestination, String pickUpPlaceName, String destinationPlaceName,String carModel1, String date, String time, String seats, String price,String driver_message1, String directionMode) {
////        this.taskCallback = (TaskLoadedCallback) mContext;
//        this.directionMode = directionMode;
//        this.mMap = map;
//        this.clickButton = clickButton;
//        this.context =context;
//        this.latLngCurrent = latLngCurrent;
//        this.latLngDestination = latLngDestination;
//        this.date = date;
//        this.time = time;
//        this.seats = seats;
//        this.price = price;
//        this.carModel1 = carModel1;
//        this.driver_message1 = driver_message1;
//        this.pickUpPlaceName =pickUpPlaceName;
//        this.destinationPlaceName = destinationPlaceName;
//        Toast.makeText(context, "in poinsts parser", Toast.LENGTH_SHORT).show();
//    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("mylog", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);


            Log.d("mylog", "Executing routes");
            Log.d("mylog", routes.toString());

        } catch (Exception e) {
            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//        mMap.clear();
//        Marker marker = mMap.addMarker(new MarkerOptions().position(latLngCurrent));
//        Marker markerDes = mMap.addMarker(new MarkerOptions().position(latLngDestination));

        if(clickButton.equals("saveRoute")){


            Map map = new HashMap();
            map.put("routes", result);
            map.put("driver_origin_lat", latLngCurrent.latitude);
            map.put("driver_origin_long", latLngCurrent.longitude);
            map.put("driver_destination_lat", latLngDestination.latitude);
            map.put("driver_destination_long", latLngDestination.longitude);

            map.put("driver_origin_name", pickUpPlaceName);
            map.put("driver_destination_name", destinationPlaceName);
            map.put("vehicle_Model1", carModel1);

            map.put("start_ride_date", date);
            map.put("start_ride_time", time);
            map.put("no_of_seats", seats);
            map.put("no_of_available_seats", seats);
            map.put("driver_message", driver_message1);


            map.put("price_per_km", price);




            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserUid = currentUser.getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Available Routs").child(currentUserUid).setValue(map);
            Toast.makeText(context, "data inserted", Toast.LENGTH_SHORT).show();


        }

        if(clickButton.equals("showRoute")){
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                if (directionMode.equalsIgnoreCase("walking")) {
                    lineOptions.width(10);
                    lineOptions.color(Color.MAGENTA);
                } else {
                    lineOptions.width(20);
                    lineOptions.color(Color.BLUE);
                }
                Log.d("mylog", "onPostExecute lineoptions decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {


                currentPolyline = mMap.addPolyline(lineOptions);

                Log.d("mylog", "add polyline");
//            taskCallback.onTaskDone(lineOptions);
//            if (currentPolyline != null){
//            currentPolyline.remove();
//            currentPolyline = map.addPolyline(lineOptions);
//        }



            } else {
                Log.d("mylog", "without Polylines drawn");
            }
        }






    }

//    RoutesObserver routesObserver1;
//
//
//    public void confirm(RoutesObserver routesObserver){
//        routesObserver1=routesObserver;
//
//    }

}
