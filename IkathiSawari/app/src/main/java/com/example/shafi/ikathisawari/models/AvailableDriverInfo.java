package com.example.shafi.ikathisawari.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AvailableDriverInfo  implements Parcelable{
    String driverKey;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;
    String time, seats, price;


    public AvailableDriverInfo(String driverKey, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo, String time, String seats, String price) {
        this.driverKey = driverKey;
        this.driverInfo = driverInfo;
        this.driverRoutInfo = driverRoutInfo;
        this.time = time;
        this.seats = seats;
        this.price = price;
    }


    protected AvailableDriverInfo(Parcel in) {
        driverKey = in.readString();
        time = in.readString();
        seats = in.readString();
        price = in.readString();
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

    @Override
    public String toString() {
        return "AvailableDriverInfo{" +
                "driverKey='" + driverKey + '\'' +
                ", driverInfo=" + driverInfo +
                ", driverRoutInfo=" + driverRoutInfo +
                ", time='" + time + '\'' +
                ", seats='" + seats + '\'' +
                ", price='" + price + '\'' +
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
    }
}

