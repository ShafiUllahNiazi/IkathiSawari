package com.example.shafi.ikathisawari.models;

public class DriverInfo {


    private String uid,name, email, mobile, cnic,gender, dob ,image;
    private float rating;

    public DriverInfo() {
    }

    public DriverInfo(String uid, String name, String email, String mobile, String cnic,String dob, String gender ,String image,float rating) {
        this.uid = uid;
        this.image = image;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.cnic = cnic;
        this.dob = dob;
        this.gender = gender;
        this.rating= rating;

    }

    public String getUid() {
        return uid;
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
                ", image='" + image + '\'' +
                ", rating=" + rating +
                '}';
    }
}
