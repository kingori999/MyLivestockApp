package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditNutritionActivity extends AppCompatActivity {

    private Spinner spinnerLivestock;
    private EditText editTextFeedType;
    private EditText editTextQuantity;
    private TimePicker timePicker;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private NutritionViewModel nutritionViewModel;
    private String nutritionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_nutrition);

        spinnerLivestock = findViewById(R.id.spinner_livestock);
        editTextFeedType = findViewById(R.id.edit_text_feed_type);
        editTextQuantity = findViewById(R.id.edit_text_quantity);
        timePicker = findViewById(R.id.time_picker);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        nutritionViewModel = new ViewModelProvider(this).get(NutritionViewModel.class);

        // Load livestock names into the spinner
        loadLivestockIntoSpinner();

        // Check if editing an existing record
        if (getIntent().hasExtra("nutritionId")) {
            nutritionId = getIntent().getStringExtra("nutritionId");
            loadNutritionData(nutritionId);
        }

        buttonSave.setOnClickListener(v -> saveNutritionRecord());
    }

    private void loadLivestockIntoSpinner() {
        db.collection("livestock")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> livestockNames = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("name");
                        livestockNames.add(name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, livestockNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLivestock.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditNutritionActivity.this, "Failed to load livestock", Toast.LENGTH_SHORT).show());
    }

    private void loadNutritionData(String nutritionId) {
        db.collection("nutrition").document(nutritionId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String livestockName = documentSnapshot.getString("livestockName");
                        String feedType = documentSnapshot.getString("feedType");
                        String quantity = documentSnapshot.getString("quantity");
                        String dateTime = documentSnapshot.getString("dateTime");

                        // Populate the fields with the existing data
                        editTextFeedType.setText(feedType);
                        editTextQuantity.setText(quantity);

                        // Set livestock spinner to correct value
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerLivestock.getAdapter();
                        int position = adapter.getPosition(livestockName);
                        spinnerLivestock.setSelection(position);

                        // Parse the dateTime and set the TimePicker
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            Date date = sdf.parse(dateTime);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditNutritionActivity.this, "Failed to load nutrition data", Toast.LENGTH_SHORT).show());
    }

    private void saveNutritionRecord() {
        String feedType = editTextFeedType.getText().toString().trim();
        String quantity = editTextQuantity.getText().toString().trim();
        String selectedLivestock = spinnerLivestock.getSelectedItem().toString();

        if (TextUtils.isEmpty(feedType) || TextUtils.isEmpty(quantity)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateTime = sdf.format(calendar.getTime());

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> nutritionRecord = new HashMap<>();
        nutritionRecord.put("livestockName", selectedLivestock);
        nutritionRecord.put("feedType", feedType);
        nutritionRecord.put("quantity", quantity);
        nutritionRecord.put("dateTime", dateTime);
        nutritionRecord.put("userId", userId);

        if (nutritionId != null) {
            db.collection("nutrition").document(nutritionId)
                    .set(nutritionRecord)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddEditNutritionActivity.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddEditNutritionActivity.this, "Error updating record", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("nutrition")
                    .add(nutritionRecord)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddEditNutritionActivity.this, "Record added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddEditNutritionActivity.this, "Error adding record", Toast.LENGTH_SHORT).show());
        }
    }
}
