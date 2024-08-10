package com.example.mylivestock;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class MilkViewModel extends AndroidViewModel {

    private MilkRepository repository;
    private LiveData<List<MilkRecord>> allMilkRecords;

    public MilkViewModel(@NonNull Application application) {
        super(application);
        repository = new MilkRepository(application);
        allMilkRecords = repository.getAllMilkRecords();
    }

    public void insert(MilkRecord milkRecord) {
        repository.insert(milkRecord);
    }

    public void update(MilkRecord milkRecord) {
        repository.update(milkRecord);
    }

    public void delete(MilkRecord milkRecord) {
        repository.delete(milkRecord);
    }

    public LiveData<List<MilkRecord>> getAllMilkRecords() {
        return allMilkRecords;
    }
}
