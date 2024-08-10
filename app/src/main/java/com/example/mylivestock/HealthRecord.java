package com.example.mylivestock;

public class HealthRecord {
    private String id;
    private String livestockId;
    private String date;
    private String nextCheckupDate;
    private String healthStatus;
    private String treatmentAdministered;
    private String veterinarianName;
    private String veterinarianPhone;

    // Default constructor for Firebase
    public HealthRecord() {}

    public HealthRecord(String livestockId, String date, String nextCheckupDate, String healthStatus, String treatmentAdministered, String veterinarianName, String veterinarianPhone) {
        this.livestockId = livestockId;
        this.date = date;
        this.nextCheckupDate = nextCheckupDate;
        this.healthStatus = healthStatus;
        this.treatmentAdministered = treatmentAdministered;
        this.veterinarianName = veterinarianName;
        this.veterinarianPhone = veterinarianPhone;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLivestockId() {
        return livestockId;
    }

    public void setLivestockId(String livestockId) {
        this.livestockId = livestockId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNextCheckupDate() {
        return nextCheckupDate;
    }

    public void setNextCheckupDate(String nextCheckupDate) {
        this.nextCheckupDate = nextCheckupDate;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getTreatmentAdministered() {
        return treatmentAdministered;
    }

    public void setTreatmentAdministered(String treatmentAdministered) {
        this.treatmentAdministered = treatmentAdministered;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public void setVeterinarianName(String veterinarianName) {
        this.veterinarianName = veterinarianName;
    }

    public String getVeterinarianPhone() {
        return veterinarianPhone;
    }

    public void setVeterinarianPhone(String veterinarianPhone) {
        this.veterinarianPhone = veterinarianPhone;
    }
}
