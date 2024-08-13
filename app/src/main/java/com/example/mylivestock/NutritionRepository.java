package com.example.mylivestock;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.mylivestock.Nutrition;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class NutritionRepository {

    private FirebaseFirestore db;
    private CollectionReference nutritionCollection;

    public NutritionRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        nutritionCollection = db.collection("nutrition");
    }

    public LiveData<List<Nutrition>> getAllNutrition(String userId) {
        MutableLiveData<List<Nutrition>> liveData = new MutableLiveData<>();
        nutritionCollection.whereEqualTo("userId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Nutrition> nutritionList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                Nutrition nutrition = document.toObject(Nutrition.class);
                nutrition.setId(document.getId());
                nutritionList.add(nutrition);
            }
            //log to confirm data retrieval

            liveData.setValue(nutritionList);
        }).addOnFailureListener(e -> Log.e("NutritionRepository", "Error fetching data",e));
        return liveData;
    }

    public void insert(Nutrition nutrition) {
        nutritionCollection.add(nutrition);
    }

    public void update(Nutrition nutrition) {
        nutritionCollection.document(nutrition.getId()).set(nutrition);
    }

    public void delete(Nutrition nutrition) {
        nutritionCollection.document(nutrition.getId()).delete();
    }
}