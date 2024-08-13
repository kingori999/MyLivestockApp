package com.example.mylivestock;

public class Nutrition {

    private String id;
    private String animalType;
    private String feedType;
    private String quantity;
    private String time;
    private String userId;

    public Nutrition() {
        // Required empty constructor for Firestore
    }

    public Nutrition(String animalType, String feedType, String quantity, String time, String userId) {
        this.animalType = animalType;
        this.feedType = feedType;
        this.quantity = quantity;
        this.time = time;
        this.userId = userId;
    }

    // Getters and Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}