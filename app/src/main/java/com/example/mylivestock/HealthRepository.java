package com.example.mylivestock;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class HealthRepository {

    private FirebaseFirestore db;
    private CollectionReference healthCollection;

    public HealthRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        healthCollection = db.collection("health");
    }

    public LiveData<List<Health>> getAllHealthRecords() {
        MutableLiveData<List<Health>> liveData = new MutableLiveData<>();
        healthCollection.orderBy("nextCheckupDate").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Health> healthList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                Health health = document.toObject(Health.class);
                health.setId(document.getId());
                healthList.add(health);
            }
            liveData.setValue(healthList);
        });
        return liveData;
    }

    public void insert(Health health) {
        healthCollection.add(health);
    }

    public void update(Health health) {
        healthCollection.document(health.getId()).set(health);
    }

    public void delete(Health health) {
        healthCollection.document(health.getId()).delete();
    }
}
