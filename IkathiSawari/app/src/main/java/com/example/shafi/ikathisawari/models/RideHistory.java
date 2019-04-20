package com.example.shafi.ikathisawari.models;

public class RideHistory {

    MakeRequest makeRequest;
    String status;
    String rejected_by;

    public RideHistory() {
    }

    public RideHistory(MakeRequest makeRequest, String status, String rejected_by) {
        this.makeRequest = makeRequest;
        this.status = status;
        this.rejected_by = rejected_by;
    }

    public MakeRequest getMakeRequest() {
        return makeRequest;
    }

    public String getStatus() {
        return status;
    }

    public String getRejected_by() {
        return rejected_by;
    }
}
