package com.example.ajiraapp;

public class JobRequest {
    private String firstName;
    private String lastName;
    private String clientLocation;
    private String clientRating;
    private String clientPhoneNumber;
    private String jobId;
    private String expertPhoneNumber;
    private String status;


    public JobRequest() {
    }

    public JobRequest(String firstName, String lastName, String clientLocation,
                      String clientRating, String clientPhoneNumber,
                      String jobId, String expertPhoneNumber, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientLocation = clientLocation;
        this.clientRating = clientRating;
        this.clientPhoneNumber = clientPhoneNumber;
        this.jobId = jobId;
        this.expertPhoneNumber = expertPhoneNumber;
        this.status = status;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getClientLocation() {
        return clientLocation;
    }

    public String getClientRating() {
        return clientRating;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber; // Getter for client phone number
    }

    public String getJobId() {
        return jobId; // Getter for job ID
    }

    public String getExpertPhoneNumber() {
        return expertPhoneNumber; // Getter for expert phone number
    }

    public String getStatus() {
        return status; // Getter for status
    }
}
