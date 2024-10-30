package com.example.ajiraapp;

public class Jobs {

    private String jobId;
    private String clientPhoneNumber;
    private String expertPhoneNumber;
    private String status;

    public Jobs() {
    }

    public Jobs(String jobId, String clientId, String expertId, String status) {
        this.jobId = jobId;
        this.clientPhoneNumber = clientId;
        this.expertPhoneNumber = expertId;
        this.status = status;
    }

    // Getters and Setters
    public String getjobId() {
        return jobId;
    }

    public void setjobId(String jobId) {
        this.jobId = jobId;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientId) {
        this.clientPhoneNumber= clientId;
    }

    public String getExpertPhoneNumber() {
        return expertPhoneNumber;
    }

    public void setExpertPhoneNumber(String expertId) {
        this.expertPhoneNumber
                = expertId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}