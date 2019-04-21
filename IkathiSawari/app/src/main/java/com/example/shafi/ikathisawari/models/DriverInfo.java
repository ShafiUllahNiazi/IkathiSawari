package com.example.shafi.ikathisawari.models;

public class DriverInfo {


    private String uid,name, email, mobile, cnic,gender, dob ,image;

    public DriverInfo() {
    }

    public DriverInfo(String uid, String name, String email, String mobile, String cnic,String dob, String gender ,String image) {
        this.uid = uid;
        this.image = image;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.cnic = cnic;
        this.dob = dob;
        this.gender = gender;

    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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
        return "DriverInfo{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cnic='" + cnic + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
