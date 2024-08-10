package com.example.mylivestock;

public class MilkRecord {

    private String id;
    private String livestockName;
    private String productionDate;
    private String sessionTime;
    private double quantity;
    private String userId;

    public MilkRecord() {
        // Default constructor required for Firestore
    }

    public MilkRecord(String livestockName, String productionDate, String sessionTime, double quantity, String userId) {
        this.livestockName = livestockName;
        this.productionDate = productionDate;
        this.sessionTime = sessionTime;
        this.quantity = quantity;
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

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
