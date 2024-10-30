package com.example.ajiraapp;

public class JobRequest {
    private String firstName;
    private String lastName;
    private String clientLocation;
    private String clientRating;
    private String clientPhoneNumber; // New field for client phone number
    private String jobId;              // New field for job ID
    private String expertPhoneNumber;  // New field for expert phone number
    private String status;             // New field for job status

    // No-argument constructor required for Firebase
    public JobRequest() {
    }

    // Constructor with parameters
    public JobRequest(String firstName, String lastName, String clientLocation,
                      String clientRating, String clientPhoneNumber,
                      String jobId, String expertPhoneNumber, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientLocation = clientLocation;
        this.clientRating = clientRating;
        this.clientPhoneNumber = clientPhoneNumber; // Initialize the new field
        this.jobId = jobId;                         // Initialize job ID
        this.expertPhoneNumber = expertPhoneNumber; // Initialize expert phone number
        this.status = status;                       // Initialize status
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
