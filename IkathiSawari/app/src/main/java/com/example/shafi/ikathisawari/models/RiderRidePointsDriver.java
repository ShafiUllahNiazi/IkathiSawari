package com.example.shafi.ikathisawari.models;

public class RiderRidePointsDriver {
    Double lat;
    Double lng;

    public RiderRidePointsDriver() {
    }

    public RiderRidePointsDriver(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "RiderRidePointsDriver{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
