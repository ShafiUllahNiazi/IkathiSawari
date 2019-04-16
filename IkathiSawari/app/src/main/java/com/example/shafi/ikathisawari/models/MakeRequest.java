package com.example.shafi.ikathisawari.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class MakeRequest {
    String current_rider;
    RiderInfo riderInfo;

    String status;
    String driverId;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;
    double riderLatOriginAtRoad,riderLngOriginAtRoad,riderLatDestinationAtRoad,  riderLngDestinationAtRoad;
    String timeAndDateRider, seatsRider;


    public MakeRequest() {
    }

    public MakeRequest(String status, String current_rider, RiderInfo riderInfo, double riderLatOriginAtRoad, double riderLngOriginAtRoad, double riderLatDestinationAtRoad, double riderLngDestinationAtRoad, String timeAndDateRider, String seatsRider, String driverId, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo) {
        this.status = status;
        this.current_rider = current_rider;
        this.riderInfo = riderInfo;
        this.riderLatOriginAtRoad = riderLatOriginAtRoad;
        this.riderLngOriginAtRoad = riderLngOriginAtRoad;
        this.riderLatDestinationAtRoad = riderLatDestinationAtRoad;
        this.riderLngDestinationAtRoad = riderLngDestinationAtRoad;
        this.timeAndDateRider = timeAndDateRider;
        this.seatsRider = seatsRider;
        this.driverId = driverId;
        this.driverInfo = driverInfo;
        this.driverRoutInfo = driverRoutInfo;

    }

    public String getCurrent_rider() {
        return current_rider;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public String getStatus() {
        return status;
    }


    public String getDriverId() {
        return driverId;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public DriverRoutInfo getDriverRoutInfo() {
        return driverRoutInfo;
    }

    public double getRiderLatOriginAtRoad() {
        return riderLatOriginAtRoad;
    }

    public double getRiderLngOriginAtRoad() {
        return riderLngOriginAtRoad;
    }

    public double getRiderLatDestinationAtRoad() {
        return riderLatDestinationAtRoad;
    }

    public double getRiderLngDestinationAtRoad() {
        return riderLngDestinationAtRoad;
    }

    public String getTimeAndDateRider() {
        return timeAndDateRider;
    }

    public String getSeatsRider() {
        return seatsRider;
    }
}
