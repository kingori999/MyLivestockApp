package com.example.mylivestock;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {

    private FirebaseFirestore db;
    private CollectionReference reportCollection;

    public ReportRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        reportCollection = db.collection("reports");
    }

    public LiveData<List<Report>> getAllReports() {
        MutableLiveData<List<Report>> liveData = new MutableLiveData<>();
        reportCollection.orderBy("dateRange").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Report> reportList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                Report report = document.toObject(Report.class);
                report.setId(document.getId());
                reportList.add(report);
            }
            liveData.setValue(reportList);
        });
        return liveData;
    }

    public void insert(Report report) {
        reportCollection.add(report);
    }

    public void update(Report report) {
        reportCollection.document(report.getId()).set(report);
    }

    public void delete(Report report) {
        reportCollection.document(report.getId()).delete();
    }
}
