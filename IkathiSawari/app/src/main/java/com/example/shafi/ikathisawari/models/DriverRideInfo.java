package com.example.shafi.ikathisawari.models;

import java.util.ArrayList;

public class DriverRideInfo {

    ArrayList<RiderRidePointsDriver> driverLiveRide;


    public DriverRideInfo(ArrayList<RiderRidePointsDriver> driverLiveRide) {
    }

    public ArrayList<RiderRidePointsDriver> getDriverLiveRide() {
        return driverLiveRide;
    }

    @Override
    public String toString() {
        return "DriverRideInfo{" +
                "driverLiveRide=" + driverLiveRide +
                '}';
    }
}
