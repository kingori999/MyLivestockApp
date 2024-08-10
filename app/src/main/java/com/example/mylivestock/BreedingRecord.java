package com.example.mylivestock;

public class BreedingRecord {

    private String id;
    private String femaleLivestockName;
    private String maleLivestockName;
    private String breedingDate;
    private String expectedDueDate;
    private String method; // Natural or Artificial Insemination
    private String userId;

    public BreedingRecord() {
        // Default constructor required for Firestore
    }

    public BreedingRecord(String femaleLivestockName, String maleLivestockName, String breedingDate, String expectedDueDate, String method, String userId) {
        this.femaleLivestockName = femaleLivestockName;
        this.maleLivestockName = maleLivestockName;
        this.breedingDate = breedingDate;
        this.expectedDueDate = expectedDueDate;
        this.method = method;
        this.userId = userId;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFemaleLivestockName() {
        return femaleLivestockName;
    }

    public void setFemaleLivestockName(String femaleLivestockName) {
        this.femaleLivestockName = femaleLivestockName;
    }

    public String getMaleLivestockName() {
        return maleLivestockName;
    }

    public void setMaleLivestockName(String maleLivestockName) {
        this.maleLivestockName = maleLivestockName;
    }

    public String getBreedingDate() {
        return breedingDate;
    }

    public void setBreedingDate(String breedingDate) {
        this.breedingDate = breedingDate;
    }

    public String getExpectedDueDate() {
        return expectedDueDate;
    }

    public void setExpectedDueDate(String expectedDueDate) {
        this.expectedDueDate = expectedDueDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
