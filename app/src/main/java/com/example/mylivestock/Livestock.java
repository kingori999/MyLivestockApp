package com.example.mylivestock;

public class Livestock {
    private String id;
    private String name;
    private String type;
    private String breed;
    private String healthStatus;
    private String userId;
    private String gender;
    private String birthOrPurchaseStatus;
    private String birthOrPurchaseDate;// Added field

    // Constructor, getters, and setters...

    public Livestock() {
        // Empty constructor needed for Firestore
    }

    public Livestock(String name, String type, String breed, String healthStatus, String userId, String gender, String birthOrPurchaseStatus, String birthOrPurchaseDate) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.healthStatus = healthStatus;
        this.userId = userId;
        this.gender = gender;
        this.birthOrPurchaseStatus = birthOrPurchaseStatus;
        this.birthOrPurchaseDate = birthOrPurchaseDate;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthOrPurchaseStatus() {
        return birthOrPurchaseStatus;
    }

    public void setBirthOrPurchaseStatus(String birthOrPurchaseStatus) {
        this.birthOrPurchaseStatus = birthOrPurchaseStatus;
    }

    public String getBirthOrPurchaseDate() {
        return birthOrPurchaseDate;
    }

    public void setBirthOrPurchaseDate(String birthOrPurchaseDate) {
        this.birthOrPurchaseDate = birthOrPurchaseDate;
    }
}