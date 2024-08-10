package com.example.mylivestock;

public class Health {

    private String id;
    private String livestockName;
    private String healthStatus;
    private String treatment;
    private String vetName;
    private String vetPhone;
    private String nextCheckupDate;
    private String userId;

    public Health() {
        // Default constructor required for calls to DataSnapshot.getValue(Health.class)
    }

    public Health(String livestockName, String healthStatus, String treatment, String vetName, String vetPhone, String nextCheckupDate, String userId) {
        this.livestockName = livestockName;
        this.healthStatus = healthStatus;
        this.treatment = treatment;
        this.vetName = vetName;
        this.vetPhone = vetPhone;
        this.nextCheckupDate = nextCheckupDate;
        this.userId = userId;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLivestockName() {
        return livestockName;
    }

    public void setLivestockName(String livestockName) {
        this.livestockName = livestockName;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getVetPhone() {
        return vetPhone;
    }

    public void setVetPhone(String vetPhone) {
        this.vetPhone = vetPhone;
    }

    public String getNextCheckupDate() {
        return nextCheckupDate;
    }

    public void setNextCheckupDate(String nextCheckupDate) {
        this.nextCheckupDate = nextCheckupDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
