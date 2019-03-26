package com.example.shafi.ikathisawari.models;

public class RiderInfo {

    private String name, email, mobile, cnic,uid;
    public RiderInfo() {
    }

    public RiderInfo(String uid,String name, String email, String mobile, String cnic) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.cnic = cnic;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Override
    public String toString() {
        return "RiderInfo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cnic='" + cnic + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
