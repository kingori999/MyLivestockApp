package com.example.mylivestock;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class BreedingViewModel extends AndroidViewModel {

    private BreedingRepository repository;
    private LiveData<List<BreedingRecord>> allBreedingRecords;

    public BreedingViewModel(@NonNull Application application) {
        super(application);
        repository = new BreedingRepository(application);

    }

    public void insert(BreedingRecord breedingRecord) {
        repository.insert(breedingRecord);
    }

    public void update(BreedingRecord breedingRecord) {
        repository.update(breedingRecord);
    }

    public void delete(BreedingRecord breedingRecord) {
        repository.delete(breedingRecord);
    }

    public LiveData<List<BreedingRecord>> getAllBreedingRecords(String userId) {
        return repository.getAllBreedingRecords(userId);
    }
}
