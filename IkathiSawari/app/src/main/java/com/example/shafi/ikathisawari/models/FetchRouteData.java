package com.example.shafi.ikathisawari.models;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class FetchRouteData {
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
    ProgressDialog progressDialog;

    public FetchRouteData() {
    }

    public FetchRouteData(GoogleMap map, String clickButton, Context context, LatLng latLngCurrent,
                          LatLng latLngDestination, String pickUpPlaceName, String destinationPlaceName, String carModel1, String date, String time, String seats, String price, String driver_message1,ProgressDialog progressDialog) {
        this.mMap = map;
        this.clickButton = clickButton;
        this.context = context;
        this.latLngCurrent = latLngCurrent;
        this.latLngDestination = latLngDestination;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.price = price;
        this.pickUpPlaceName =pickUpPlaceName;
        this.destinationPlaceName = destinationPlaceName;
        this.carModel1 = carModel1;
        this.driver_message1 = driver_message1;
        this.progressDialog =progressDialog;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public Polyline getCurrentPolyline() {
        return currentPolyline;
    }

    public String getDirectionMode() {
        return directionMode;
    }

    public String getClickButton() {
        return clickButton;
    }

    public Context getContext() {
        return context;
    }

    public LatLng getLatLngCurrent() {
        return latLngCurrent;
    }

    public LatLng getLatLngDestination() {
        return latLngDestination;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSeats() {
        return seats;
    }

    public String getPrice() {
        return price;
    }

    public String getPickUpPlaceName() {
        return pickUpPlaceName;
    }

    public String getDestinationPlaceName() {
        return destinationPlaceName;
    }

    public String getCarModel1() {
        return carModel1;
    }

    public String getDriver_message1() {
        return driver_message1;
    }
}
