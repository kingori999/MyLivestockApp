package com.example.mylivestock;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class LivestockRepository {

    private FirebaseFirestore db;
    private CollectionReference livestockCollection;
    private CollectionReference breedCollection;
    private FirebaseAuth auth;

    public LivestockRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        livestockCollection = db.collection("livestock");
        breedCollection = db.collection("breeds");  // Collection for breeds
    }

    public LiveData<List<Livestock>> getAllLivestock() {
        MutableLiveData<List<Livestock>> liveData = new MutableLiveData<>();
        String userId = auth.getCurrentUser().getUid();  // Get the logged-in user's ID

        // Query livestock associated with the logged-in user
        livestockCollection.whereEqualTo("userId", userId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Livestock> livestockList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Livestock livestock = doc.toObject(Livestock.class);
                        livestock.setId(doc.getId());
                        livestockList.add(livestock);
                    }
                    liveData.setValue(livestockList);
                });
        return liveData;
    }

    public void insert(Livestock livestock) {
        String userId = auth.getCurrentUser().getUid();  // Get the logged-in user's ID
        livestock.setUserId(userId);  // Associate livestock with the logged-in user

        DocumentReference newDoc = livestockCollection.document();
        livestock.setId(newDoc.getId());  // Set the document ID in the livestock object
        newDoc.set(livestock);
    }

    public void update(Livestock livestock) {
        livestockCollection.document(livestock.getId()).set(livestock);
    }

    public void delete(Livestock livestock) {
        livestockCollection.document(livestock.getId()).delete();
    }

    // Implement the insertBreed method
    public void insertBreed(Breed breed) {
        breedCollection.add(breed);
    }

    // Implement the searchLivestock method
    public LiveData<List<Livestock>> searchLivestock(String query) {
        MutableLiveData<List<Livestock>> liveData = new MutableLiveData<>();
        String userId = auth.getCurrentUser().getUid();  // Get the logged-in user's ID

        livestockCollection
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Livestock> livestockList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Livestock livestock = doc.toObject(Livestock.class);
                        livestock.setId(doc.getId());
                        // Perform a client-side filter based on the query string
                        if (livestock.getName().toLowerCase().contains(query.toLowerCase())) {
                            livestockList.add(livestock);
                        }
                    }
                    liveData.setValue(livestockList);
                });

        return liveData;
    }
}