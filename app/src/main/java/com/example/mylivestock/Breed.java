/**
 * @Author: 
 * @Date: 2024-08-09 10:40:21
 * @LastEditors: 
 * @LastEditTime: 2024-08-10 20:48:39
 * @FilePath: app/src/main/java/com/example/mylivestock/Breed.java
 * @Description: 这是默认设置,可以在设置》工具》File Description中进行配置
 */
package com.example.mylivestock;

public class Breed {

    private String animalType;
    private String breedName;

    public Breed() {
        // Required for Firebase
    }

    public Breed(String animalType, String breedName) {
        this.animalType = animalType;
        this.breedName = breedName;
    }

    // Getters and Setters
    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }
}
