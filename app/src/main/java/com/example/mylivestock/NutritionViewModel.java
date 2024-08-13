package com.example.mylivestock;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class NutritionViewModel extends AndroidViewModel {

    private NutritionRepository repository;
    private LiveData<List<Nutrition>> allNutrition;

    public NutritionViewModel(@NonNull Application application) {
        super(application);
        repository = new NutritionRepository(application);
    }

    public void insert(Nutrition nutrition) {
        repository.insert(nutrition);
    }

    public void update(Nutrition nutrition) {
        repository.update(nutrition);
    }

    public void delete(Nutrition nutrition) {
        repository.delete(nutrition);
    }

    public LiveData<List<Nutrition>> getAllNutrition(String userId) {
        return repository.getAllNutrition(userId);
    }
}