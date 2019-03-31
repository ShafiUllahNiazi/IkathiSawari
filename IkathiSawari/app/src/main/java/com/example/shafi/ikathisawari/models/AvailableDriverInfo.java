package com.example.shafi.ikathisawari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class AvailableDriverInfo implements Parcelable {
    String driverKey;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;


    public AvailableDriverInfo(String driverKey, DriverInfo driverInfo, DriverRoutInfo driverRoutInfo) {
        this.driverKey = driverKey;
        this.driverInfo = driverInfo;
        this.driverRoutInfo = driverRoutInfo;
    }


    protected AvailableDriverInfo(Parcel in) {
        driverKey = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverKey);
    }
}

