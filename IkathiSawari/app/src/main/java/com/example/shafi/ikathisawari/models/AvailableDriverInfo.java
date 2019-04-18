package com.example.shafi.ikathisawari.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class AvailableDriverInfo implements Parcelable{
    String driverKey;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;
    String time, seats, price;
    Location riderOriginAtRoad, riderDestinationAtRoad;
    String timeAndDateRider, seatsRider, priceRider;
    int traveledDistanceRider;
    int traveledTimeRider;
    int rideCharges;


    public AvailableDriverInfo(String driverKey, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo, String time, String seats, String price, Location riderOriginAtRoad, Location riderDestinationAtRoad, String timeAndDateRider, String seatsRider, int traveledDistanceRider, int traveledTimeRider, int rideCharges) {
        this.driverKey = driverKey;
        this.driverInfo = driverInfo;
        this.driverRoutInfo = driverRoutInfo;
        this.time = time;
        this.seats = seats;
        this.price = price;
        this.riderOriginAtRoad = riderOriginAtRoad;
        this.riderDestinationAtRoad = riderDestinationAtRoad;
        this.timeAndDateRider = timeAndDateRider;
        this.seatsRider = seatsRider;
        this.priceRider = seatsRider;
        this.traveledDistanceRider = traveledDistanceRider;
        this.traveledTimeRider = traveledTimeRider;
        this.rideCharges = rideCharges;
    }

    protected AvailableDriverInfo(Parcel in) {
        driverKey = in.readString();
        time = in.readString();
        seats = in.readString();
        price = in.readString();
        riderOriginAtRoad = in.readParcelable(Location.class.getClassLoader());
        riderDestinationAtRoad = in.readParcelable(Location.class.getClassLoader());
        timeAndDateRider = in.readString();
        seatsRider = in.readString();
        priceRider = in.readString();
        traveledDistanceRider = in.readInt();
        traveledTimeRider = in.readInt();
        rideCharges = in.readInt();
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

    public Location getRiderOriginAtRoad() {
        return riderOriginAtRoad;
    }

    public Location getRiderDestinationAtRoad() {
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
        dest.writeParcelable(riderOriginAtRoad, flags);
        dest.writeParcelable(riderDestinationAtRoad, flags);
        dest.writeString(timeAndDateRider);
        dest.writeString(seatsRider);
        dest.writeString(priceRider);
        dest.writeInt(traveledDistanceRider);
        dest.writeInt(traveledTimeRider);
        dest.writeInt(rideCharges);
    }
}

