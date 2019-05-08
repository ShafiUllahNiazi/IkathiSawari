package com.example.shafi.ikathisawari.models;

public class RiderInfo {

    private String name, email, mobile, cnic,uid,gender, dob,image;
    private float rating;

    public RiderInfo() {
    }

    public RiderInfo(String uid,String name, String email, String mobile, String cnic,String dob, String gender,String profileImg, float rating) {
        this.image = profileImg;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.cnic = cnic;
        this.uid = uid;
        this.dob = dob;
        this.gender = gender;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCnic() {
        return cnic;
    }

    public String getUid() {
        return uid;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }
}
