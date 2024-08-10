package com.example.mylivestock;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private FirebaseFirestore db;
    private CollectionReference tasksCollection;

    public TaskRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        tasksCollection = db.collection("tasks");
    }

    public LiveData<List<Task>> getAllTasks() {
        MutableLiveData<List<Task>> liveData = new MutableLiveData<>();
        tasksCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Task> taskList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                Task task = document.toObject(Task.class);
                task.setId(document.getId());
                taskList.add(task);
            }
            liveData.setValue(taskList);
        });
        return liveData;
    }

    public void insert(Task task) {
        tasksCollection.add(task);
    }

    public void update(Task task) {
        tasksCollection.document(task.getId()).set(task);
    }

    public void delete(Task task) {
        tasksCollection.document(task.getId()).delete();
    }
}
