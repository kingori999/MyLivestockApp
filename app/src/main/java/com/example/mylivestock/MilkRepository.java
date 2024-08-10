package com.example.mylivestock;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MilkRepository {

    private FirebaseFirestore db;
    private CollectionReference milkCollection;

    public MilkRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        milkCollection = db.collection("milkRecords");
    }

    public LiveData<List<MilkRecord>> getAllMilkRecords() {
        MutableLiveData<List<MilkRecord>> liveData = new MutableLiveData<>();
        milkCollection.orderBy("productionDate").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<MilkRecord> milkList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                MilkRecord milkRecord = document.toObject(MilkRecord.class);
                milkRecord.setId(document.getId());
                milkList.add(milkRecord);
            }
            liveData.setValue(milkList);
        });
        return liveData;
    }

    public void insert(MilkRecord milkRecord) {
        milkCollection.add(milkRecord);
    }

    public void update(MilkRecord milkRecord) {
        milkCollection.document(milkRecord.getId()).set(milkRecord);
    }

    public void delete(MilkRecord milkRecord) {
        milkCollection.document(milkRecord.getId()).delete();
    }
}
