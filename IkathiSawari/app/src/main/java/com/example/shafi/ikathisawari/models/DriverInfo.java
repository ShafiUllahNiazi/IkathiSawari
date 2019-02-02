package com.example.shafi.ikathisawari.models;

public class DriverInfo {


    private String name, email, mobile, cnic, type_and_model, registration_no, no_of_seats;

    public DriverInfo() {
    }

    public DriverInfo(String name, String email, String mobile, String cnic, String type_and_model, String registration_no, String no_of_seats) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.cnic = cnic;
        this.type_and_model = type_and_model;
        this.registration_no = registration_no;
        this.no_of_seats = no_of_seats;
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

    public String getType_and_model() {
        return type_and_model;
    }

    public void setType_and_model(String type_and_model) {
        this.type_and_model = type_and_model;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getNo_of_seats() {
        return no_of_seats;
    }

    public void setNo_of_seats(String no_of_seats) {
        this.no_of_seats = no_of_seats;
    }

    @Override
    public String toString() {
        return "DriverInfo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cnic='" + cnic + '\'' +
                ", type_and_model='" + type_and_model + '\'' +
                ", registration_no='" + registration_no + '\'' +
                ", no_of_seats='" + no_of_seats + '\'' +
                '}';
    }
}
