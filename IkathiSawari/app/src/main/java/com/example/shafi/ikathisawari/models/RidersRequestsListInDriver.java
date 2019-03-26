package com.example.shafi.ikathisawari.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RidersRequestsListInDriver implements Parcelable {
    String dateAndTime;
    RiderInfo riderInfo;

    public RidersRequestsListInDriver(String dateAndTime, RiderInfo riderInfo) {
        this.dateAndTime = dateAndTime;
        this.riderInfo = riderInfo;
    }

    protected RidersRequestsListInDriver(Parcel in) {
        dateAndTime = in.readString();
    }

    public static final Creator<RidersRequestsListInDriver> CREATOR = new Creator<RidersRequestsListInDriver>() {
        @Override
        public RidersRequestsListInDriver createFromParcel(Parcel in) {
            return new RidersRequestsListInDriver(in);
        }

        @Override
        public RidersRequestsListInDriver[] newArray(int size) {
            return new RidersRequestsListInDriver[size];
        }
    };

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public void setRiderInfo(RiderInfo riderInfo) {
        this.riderInfo = riderInfo;
    }

    @Override
    public String toString() {
        return "RidersRequestsListInDriver{" +
                "dateAndTime='" + dateAndTime + '\'' +
                ", riderInfo=" + riderInfo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateAndTime);
    }
}
