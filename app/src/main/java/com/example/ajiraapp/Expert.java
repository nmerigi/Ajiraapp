package com.example.ajiraapp;

public class Expert {
    String firstname, lastname, gender, email, dob, phonenumber, location,password, service, servicecharge, userid_upload, goodconduct_upload, fcmToken;
    private double rating;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    public void setServicecharge(String servicecharge) {
        this.servicecharge = servicecharge;
    }

    public String getUserid_upload() {
        return userid_upload;
    }

    public void setUserid_upload(String userid_upload) {
        this.userid_upload = userid_upload;
    }

    public String getGoodconduct_upload() {
        return goodconduct_upload;
    }

    public void setGoodconduct_upload(String goodconduct_upload) {
        this.goodconduct_upload = goodconduct_upload;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Expert(String firstname, String lastname, String email, String gender, String dob, String phonenumber, String location, String password, String service, String servicecharge, String userid_upload, String goodconduct_upload, double rating) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.phonenumber = phonenumber;
        this.location = location;
        this.password = password;
        this.service = service;
        this.servicecharge = servicecharge;
        this.userid_upload = userid_upload;
        this.goodconduct_upload = goodconduct_upload;
        this.rating= 5.0;

    }

    public Expert(String firstname, String lastname, String email, String gender, String dob, String phonenumber, String location, String password, String userid_upload, String goodconduct_upload, String fcmToken, double rating) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.phonenumber = phonenumber;
        this.location = location;
        this.password = password;
        this.userid_upload = userid_upload;
        this.goodconduct_upload = goodconduct_upload;
        this.fcmToken = fcmToken;
        this.rating = rating;
    }

    public Expert() {
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    // Add this for testing purposes
    public Expert(String firstname, String service, String servicecharge) {
        this.firstname = firstname;
        this.service = service;
        this.servicecharge = servicecharge;

    }

}
