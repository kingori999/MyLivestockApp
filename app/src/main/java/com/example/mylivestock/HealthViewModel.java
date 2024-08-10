package com.example.mylivestock;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class HealthViewModel extends AndroidViewModel {

    private HealthRepository repository;
    private LiveData<List<Health>> allHealthRecords;

    public HealthViewModel(@NonNull Application application) {
        super(application);
        repository = new HealthRepository(application);
        allHealthRecords = repository.getAllHealthRecords();
    }

    public void insert(Health health) {
        repository.insert(health);
    }

    public void update(Health health) {
        repository.update(health);
    }

    public void delete(Health health) {
        repository.delete(health);
    }

    public LiveData<List<Health>> getAllHealthRecords() {
        return allHealthRecords;
    }
}
