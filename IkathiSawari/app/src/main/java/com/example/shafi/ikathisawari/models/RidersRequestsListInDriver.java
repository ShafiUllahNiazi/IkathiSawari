package com.example.shafi.ikathisawari.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RidersRequestsListInDriver  implements Parcelable{
    String dateAndTime;
//    RiderInfo riderInfo;
    MakeRequest makeRequest;

    public RidersRequestsListInDriver(String dateAndTime, MakeRequest makeRequest) {
        this.dateAndTime = dateAndTime;
        this.makeRequest = makeRequest;
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

    public MakeRequest getMakeRequest() {
        return makeRequest;
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
