package com.example.shafi.ikathisawari.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class AvailableDriverInfo implements Parcelable{
    String driverKey;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;
    String time, seats, price,date;
    RiderRidePointsDriver riderOriginAtRoad, riderDestinationAtRoad;
    String timeAndDateRider, seatsRider, priceRider;
    int traveledDistanceRider;
    int traveledTimeRider;
    int rideCharges;
    String vehicle_Model1;
    String driver_origin_name;
    String driver_destination_name;
    String messagedriver;
    String messageRider;
    String rider_origin_name;
    String rider_destination_name;
    String no_of_available_seats;
    String current_Rider;
    RiderInfo riderInfo;


    public AvailableDriverInfo() {
    }

    public AvailableDriverInfo(String current_Rider, RiderInfo riderInfo, String driverKey,
                               DriverInfo driverInfo, DriverRoutInfo driverRoutInfo, String date,
                               String time, String seats, String price, RiderRidePointsDriver riderOriginAtRoad,
                               RiderRidePointsDriver riderDestinationAtRoad, String timeAndDateRider,
                               String seatsRider, int traveledDistanceRider, int traveledTimeRider,
                               int rideCharges, String vehicle_Model1, String driver_origin_name,
                               String driver_destination_name, String messagedriver, String rider_origin_name,
                               String rider_destination_name, String no_of_available_seats,String messageRider) {


        this.current_Rider =current_Rider;
        this.riderInfo = riderInfo;
        this.no_of_available_seats = no_of_available_seats;
        this.vehicle_Model1 = vehicle_Model1;
        this.rider_origin_name = rider_origin_name;
        this.rider_destination_name = rider_destination_name;
        this.messagedriver = messagedriver;
        this.driver_origin_name = driver_origin_name;
        this.driver_destination_name = driver_destination_name;
        this.driverKey = driverKey;
        this.driverInfo = driverInfo;
        this.driverRoutInfo = driverRoutInfo;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.price = price;
        this.riderOriginAtRoad = riderOriginAtRoad;
        this.riderDestinationAtRoad = riderDestinationAtRoad;
        this.timeAndDateRider = timeAndDateRider;
        this.seatsRider = seatsRider;

        this.traveledDistanceRider = traveledDistanceRider;
        this.traveledTimeRider = traveledTimeRider;
        this.rideCharges = rideCharges;
        this.messageRider = messageRider;
    }

    protected AvailableDriverInfo(Parcel in) {
        driverKey = in.readString();
        time = in.readString();
        seats = in.readString();
        price = in.readString();
        date = in.readString();
        timeAndDateRider = in.readString();
        seatsRider = in.readString();
        priceRider = in.readString();
        traveledDistanceRider = in.readInt();
        traveledTimeRider = in.readInt();
        rideCharges = in.readInt();
        vehicle_Model1 = in.readString();
        driver_origin_name = in.readString();
        driver_destination_name = in.readString();
        messagedriver = in.readString();
        messageRider = in.readString();
        rider_origin_name = in.readString();
        rider_destination_name = in.readString();
        no_of_available_seats = in.readString();
        current_Rider = in.readString();
    }

    public static final Creator<AvailableDriverInfo> CREATOR = new Creator<AvailableDriverInfo>() {
        @Override
        public AvailableDriverInfo createFromParcel(Parcel in) {
            return new AvailableDriverInfo(in);
        }

        @Override
        public AvailableDriverInfo[] newArray(int size) {
            return new AvailableDriverInfo[size];
        }
    };

    public void setMessageRider(String messageRider) {
        this.messageRider = messageRider;
    }

    public String getDriverKey() {
        return driverKey;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public DriverRoutInfo getDriverRoutInfo() {
        return driverRoutInfo;
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

    public String getDate() {
        return date;
    }

    public RiderRidePointsDriver getRiderOriginAtRoad() {
        return riderOriginAtRoad;
    }

    public RiderRidePointsDriver getRiderDestinationAtRoad() {
        return riderDestinationAtRoad;
    }

    public String getTimeAndDateRider() {
        return timeAndDateRider;
    }

    public String getSeatsRider() {
        return seatsRider;
    }

    public String getPriceRider() {
        return priceRider;
    }

    public int getTraveledDistanceRider() {
        return traveledDistanceRider;
    }

    public int getTraveledTimeRider() {
        return traveledTimeRider;
    }

    public int getRideCharges() {
        return rideCharges;
    }

    public String getVehicle_Model1() {
        return vehicle_Model1;
    }

    public String getDriver_origin_name() {
        return driver_origin_name;
    }

    public String getDriver_destination_name() {
        return driver_destination_name;
    }

    public String getMessagedriver() {
        return messagedriver;
    }

    public String getMessageRider() {
        return messageRider;
    }

    public String getRider_origin_name() {
        return rider_origin_name;
    }

    public String getRider_destination_name() {
        return rider_destination_name;
    }

    public String getNo_of_available_seats() {
        return no_of_available_seats;
    }

    public String getCurrent_Rider() {
        return current_Rider;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverKey);
        dest.writeString(time);
        dest.writeString(seats);
        dest.writeString(price);
        dest.writeString(date);
        dest.writeString(timeAndDateRider);
        dest.writeString(seatsRider);
        dest.writeString(priceRider);
        dest.writeInt(traveledDistanceRider);
        dest.writeInt(traveledTimeRider);
        dest.writeInt(rideCharges);
        dest.writeString(vehicle_Model1);
        dest.writeString(driver_origin_name);
        dest.writeString(driver_destination_name);
        dest.writeString(messagedriver);
        dest.writeString(messageRider);
        dest.writeString(rider_origin_name);
        dest.writeString(rider_destination_name);
        dest.writeString(no_of_available_seats);
        dest.writeString(current_Rider);
    }
}

