package com.example.shafi.ikathisawari.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class AvailableDriverInfo  implements Parcelable{
    String driverKey;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;
    String time, seats, price;
    Location riderOriginAtRoad, riderDestinationAtRoad;
    String timeAndDateRider, seatsRider, priceRider;


    public AvailableDriverInfo(String driverKey, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo, String time, String seats, String price, Location riderOriginAtRoad, Location riderDestinationAtRoad, String timeAndDateRider, String seatsRider) {
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

    @Override
    public String toString() {
        return "AvailableDriverInfo{" +
                "driverKey='" + driverKey + '\'' +
                ", driverInfo=" + driverInfo +
                ", driverRoutInfo=" + driverRoutInfo +
                ", time='" + time + '\'' +
                ", seats='" + seats + '\'' +
                ", price='" + price + '\'' +
                ", riderOriginAtRoad=" + riderOriginAtRoad +
                ", riderDestinationAtRoad=" + riderDestinationAtRoad +
                ", timeAndDateRider='" + timeAndDateRider + '\'' +
                ", seatsRider='" + seatsRider + '\'' +
                ", priceRider='" + priceRider + '\'' +
                '}';
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
    }
}

