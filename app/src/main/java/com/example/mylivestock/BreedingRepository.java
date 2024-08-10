package com.example.mylivestock;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class BreedingRepository {

    private FirebaseFirestore db;
    private CollectionReference breedingCollection;

    public BreedingRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        breedingCollection = db.collection("breedingRecords");
    }

    public LiveData<List<BreedingRecord>> getAllBreedingRecords() {
        MutableLiveData<List<BreedingRecord>> liveData = new MutableLiveData<>();
        breedingCollection.orderBy("breedingDate").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<BreedingRecord> breedingList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                BreedingRecord breedingRecord = document.toObject(BreedingRecord.class);
                breedingRecord.setId(document.getId());
                breedingList.add(breedingRecord);
            }
            liveData.setValue(breedingList);
        });
        return liveData;
    }

    public void insert(BreedingRecord breedingRecord) {
        breedingCollection.add(breedingRecord);
    }

    public void update(BreedingRecord breedingRecord) {
        breedingCollection.document(breedingRecord.getId()).set(breedingRecord);
    }

    public void delete(BreedingRecord breedingRecord) {
        breedingCollection.document(breedingRecord.getId()).delete();
    }
}
