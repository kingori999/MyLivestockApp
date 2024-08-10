package com.example.mylivestock;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LivestockViewModel extends AndroidViewModel {

    private LivestockRepository repository;
    private LiveData<List<Livestock>> allLivestock;

    public LivestockViewModel(@NonNull Application application) {
        super(application);
        repository = new LivestockRepository(application);
        allLivestock = repository.getAllLivestock();
    }

    public void insert(Livestock livestock) {
        repository.insert(livestock);
    }

    public void update(Livestock livestock) {
        repository.update(livestock);
    }

    public void delete(Livestock livestock) {
        repository.delete(livestock);
    }

    public LiveData<List<Livestock>> getAllLivestock() {
        return allLivestock;
    }

    // Add the insertBreed method
    public void insertBreed(Breed breed) {
        repository.insertBreed(breed);
    }

    // Add the searchLivestock method
    public LiveData<List<Livestock>> searchLivestock(String query) {
        return repository.searchLivestock(query);
    }
}