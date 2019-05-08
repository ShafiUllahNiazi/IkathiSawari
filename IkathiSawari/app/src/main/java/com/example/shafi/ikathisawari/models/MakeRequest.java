package com.example.shafi.ikathisawari.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class MakeRequest {
//    String current_rider;
//    RiderInfo riderInfo;
//
//    String status;
//    String driverId;
//    DriverInfo driverInfo;
//    DriverRoutInfo driverRoutInfo;
//    double riderLatOriginAtRoad,riderLngOriginAtRoad,riderLatDestinationAtRoad,  riderLngDestinationAtRoad;
//    String timeAndDateRider, seatsRider;
//    int traveledDistanceRider, traveledTimeRider;
//    int rideCharges;

    String status;
    boolean isChargesReceived;

    AvailableDriverInfo availableDriverInfo;


    public MakeRequest() {
    }

//    public MakeRequest(String status, String current_rider, RiderInfo riderInfo, double riderLatOriginAtRoad, double riderLngOriginAtRoad, double riderLatDestinationAtRoad, double riderLngDestinationAtRoad, String timeAndDateRider, String seatsRider, String driverId, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo,int traveledDistanceRider, int traveledTimeRider,int rideCharges) {
//        this.status = status;
//        this.current_rider = current_rider;
//        this.riderInfo = riderInfo;
//        this.riderLatOriginAtRoad = riderLatOriginAtRoad;
//        this.riderLngOriginAtRoad = riderLngOriginAtRoad;
//        this.riderLatDestinationAtRoad = riderLatDestinationAtRoad;
//        this.riderLngDestinationAtRoad = riderLngDestinationAtRoad;
//        this.timeAndDateRider = timeAndDateRider;
//        this.seatsRider = seatsRider;
//        this.driverId = driverId;
//        this.driverInfo = driverInfo;
//        this.driverRoutInfo = driverRoutInfo;
//        this.traveledDistanceRider = traveledDistanceRider;
//        this.traveledTimeRider = traveledTimeRider;
//        this.rideCharges = rideCharges;
//
//    }

    public MakeRequest(String status,boolean isChargesReceived, AvailableDriverInfo availableDriverInfo) {

        this.status=status;
        this.isChargesReceived = isChargesReceived;
        this.availableDriverInfo = availableDriverInfo;
    }

    public String getStatus() {
        return status;
    }

    public boolean isChargesReceived() {
        return isChargesReceived;
    }

    public AvailableDriverInfo getAvailableDriverInfo() {
        return availableDriverInfo;
    }
}
