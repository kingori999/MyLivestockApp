package com.example.mylivestock;

public class Nutrition {

    private String id;
    private String livestockName;
    private String feedType;
    private String quantity;
    private String dateTime;
    private String userId;

    public Nutrition() {
        // Default constructor required for calls to DataSnapshot.getValue(Nutrition.class)
    }

    public Nutrition(String livestockName, String feedType, String quantity, String dateTime, String userId) {
        this.livestockName = livestockName;
        this.feedType = feedType;
        this.quantity = quantity;
        this.dateTime = dateTime;
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

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
