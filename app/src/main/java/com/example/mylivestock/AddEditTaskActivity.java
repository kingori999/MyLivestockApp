package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTextTaskTitle, editTextTaskDescription;
    private DatePicker datePickerDeadline;
    private TimePicker timePickerDeadline;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TaskViewModel taskViewModel;
    private String taskId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTaskTitle = findViewById(R.id.edit_text_task_title);
        editTextTaskDescription = findViewById(R.id.edit_text_task_description);
        datePickerDeadline = findViewById(R.id.date_picker_deadline);
        timePickerDeadline = findViewById(R.id.time_picker_deadline);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Check if editing an existing record
        if (getIntent().hasExtra("taskId")) {
            taskId = getIntent().getStringExtra("taskId");
            loadTaskData(taskId);
        }

        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void loadTaskData(String taskId) {
        db.collection("tasks").document(taskId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String taskTitle = documentSnapshot.getString("taskTitle");
                        String taskDescription = documentSnapshot.getString("taskDescription");
                        String deadline = documentSnapshot.getString("deadline");

                        editTextTaskTitle.setText(taskTitle);
                        editTextTaskDescription.setText(taskDescription);

                        // Parse and set the Deadline
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(sdf.parse(deadline));
                            datePickerDeadline.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            timePickerDeadline.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                            timePickerDeadline.setCurrentMinute(calendar.get(Calendar.MINUTE));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditTaskActivity.this, "Failed to load task data", Toast.LENGTH_SHORT).show());
    }

    private void saveTask() {
        String taskTitle = editTextTaskTitle.getText().toString().trim();
        String taskDescription = editTextTaskDescription.getText().toString().trim();

        if (TextUtils.isEmpty(taskTitle) || TextUtils.isEmpty(taskDescription)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the Deadline Date and Time from DatePicker and TimePicker
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePickerDeadline.getYear(), datePickerDeadline.getMonth(), datePickerDeadline.getDayOfMonth(),
                timePickerDeadline.getCurrentHour(), timePickerDeadline.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String deadline = sdf.format(calendar.getTime());

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> taskRecord = new HashMap<>();
        taskRecord.put("taskTitle", taskTitle);
        taskRecord.put("taskDescription", taskDescription);
        taskRecord.put("deadline", deadline);
        taskRecord.put("userId", userId);

        if (taskId != null) {
            db.collection("tasks").document(taskId)
                    .set(taskRecord)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddEditTaskActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddEditTaskActivity.this, "Error updating task", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("tasks")
                    .add(taskRecord)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddEditTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddEditTaskActivity.this, "Error adding task", Toast.LENGTH_SHORT).show());
        }
    }
}
