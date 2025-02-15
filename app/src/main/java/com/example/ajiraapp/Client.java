package com.example.ajiraapp;

public class Client {
    String firstname, lastname, gender, email, dob, phonenumber, location, password, userid_upload, goodconduct_upload, fcmToken;
    double rating;

    // Constructor for sign-up (without FCM token)
    public Client(String firstname, String lastname, String email, String gender, String dob, String phonenumber, String location, String password, String userid_upload, String goodconduct_upload, double rating) {
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
        this.rating = 5.0; // Default rating
    }

    // Constructor for login (with FCM token)
    public Client(String firstname, String lastname, String email, String gender, String dob, String phonenumber, String location, String password, String userid_upload, String goodconduct_upload, String fcmToken, double rating) {
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

    // Default constructor
    public Client() {
    }

    // Getters and Setters
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
}
