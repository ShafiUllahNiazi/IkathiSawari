package com.example.shafi.ikathisawari.models;

import com.google.android.gms.maps.model.LatLng;

public class MakeRequest {
    String current_rider;
    RiderInfo riderInfo;

    String status;
    double origin_latitude, origin_longitude,destination_latitude,destination_longitude;
    String driverKey;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;


    public MakeRequest() {
    }

    public MakeRequest(String status, String current_rider, RiderInfo riderInfo, double origin_latitude, double origin_longitude, double destination_latitude, double destination_longitude, String driverKey, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo) {
        this.status = status;
        this.current_rider = current_rider;
        this.riderInfo = riderInfo;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.destination_latitude = destination_latitude;
        this.destination_longitude = destination_longitude;
        this.driverKey = driverKey;
        this.driverInfo = driverInfo;
        this.driverRoutInfo = driverRoutInfo;

    }

    public String getStatus() {
        return status;
    }

    public String getCurrent_rider() {
        return current_rider;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public double getOrigin_latitude() {
        return origin_latitude;
    }

    public double getOrigin_longitude() {
        return origin_longitude;
    }

    public double getDestination_latitude() {
        return destination_latitude;
    }

    public double getDestination_longitude() {
        return destination_longitude;
    }
}
