package com.example.shafi.ikathisawari.directionhelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.shafi.ikathisawari.models.FetchRouteData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vishal on 10/20/2018.
 */

public class FetchURL extends AsyncTask<String, Void, String> {
    private GoogleMap mMap;
    private Polyline currentPolyline;
    String directionMode = "driving";
    String clickButton;
    Context context;
    private LatLng latLngCurrent, latLngDestination;
    String date,time, seats,price;
    String pickUpPlaceName;
    String destinationPlaceName;
    String carModel1;
    String driver_message1;
    FetchRouteData fetchRouteData;

    public FetchURL(FetchRouteData fetchRouteData) {
        this.fetchRouteData = fetchRouteData;

    }

//    public FetchURL(GoogleMap map, String clickButton, Context context, LatLng latLngCurrent,
//                    LatLng latLngDestination, String pickUpPlaceName, String destinationPlaceName, String carModel1, String date, String time, String seats, String price, String driver_message1) {
//        this.mMap = map;
//        this.clickButton = clickButton;
//        this.context = context;
//        this.latLngCurrent = latLngCurrent;
//        this.latLngDestination = latLngDestination;
//        this.date = date;
//        this.time = time;
//        this.seats = seats;
//        this.price = price;
//        this.pickUpPlaceName =pickUpPlaceName;
//        this.destinationPlaceName = destinationPlaceName;
//        this.carModel1 = carModel1;
//        this.driver_message1 = driver_message1;
//    }

//    public FetchURL(GoogleMap map, String clickButton, Context context, LatLng latLngCurrent,
//                    LatLng latLngDestination) {
//        this.mMap = map;
//        this.clickButton = clickButton;
//        this.context = context;
//        this.latLngCurrent = latLngCurrent;
//        this.latLngDestination = latLngDestination;
//    }

    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        directionMode = strings[1];
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0]);
            Log.d("mylog", "Background task data " + data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(fetchRouteData, directionMode);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("mylog", "Downloaded URL: " + data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

