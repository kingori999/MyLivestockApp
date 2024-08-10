package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddEditHealthActivity extends AppCompatActivity {

    private Spinner spinnerLivestock;
    private EditText editTextHealthStatus, editTextTreatment, editTextVetName, editTextVetPhone;
    private DatePicker datePickerNextCheckup;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private HealthViewModel healthViewModel;
    private String healthId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_health);

        spinnerLivestock = findViewById(R.id.spinner_livestock);
        editTextHealthStatus = findViewById(R.id.edit_text_health_status);
        editTextTreatment = findViewById(R.id.edit_text_treatment);
        editTextVetName = findViewById(R.id.edit_text_vet_name);
        editTextVetPhone = findViewById(R.id.edit_text_vet_phone);
        datePickerNextCheckup = findViewById(R.id.date_picker_next_checkup);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);

        // Load livestock names into the spinner
        loadLivestockIntoSpinner();

        // Check if editing an existing record
        if (getIntent().hasExtra("healthId")) {
            healthId = getIntent().getStringExtra("healthId");
            loadHealthData(healthId);
        }

        buttonSave.setOnClickListener(v -> saveHealthRecord());
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
                .addOnFailureListener(e -> Toast.makeText(AddEditHealthActivity.this, "Failed to load livestock", Toast.LENGTH_SHORT).show());
    }

    private void loadHealthData(String healthId) {
        db.collection("health").document(healthId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String livestockName = documentSnapshot.getString("livestockName");
                        String healthStatus = documentSnapshot.getString("healthStatus");
                        String treatment = documentSnapshot.getString("treatment");
                        String vetName = documentSnapshot.getString("vetName");
                        String vetPhone = documentSnapshot.getString("vetPhone");
                        String nextCheckupDate = documentSnapshot.getString("nextCheckupDate");

                        // Populate the fields with the existing data
                        editTextHealthStatus.setText(healthStatus);
                        editTextTreatment.setText(treatment);
                        editTextVetName.setText(vetName);
                        editTextVetPhone.setText(vetPhone);

                        // Set livestock spinner to correct value
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerLivestock.getAdapter();
                        int position = adapter.getPosition(livestockName);
                        spinnerLivestock.setSelection(position);

                        // Parse and set the Next Checkup Date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date = sdf.parse(nextCheckupDate);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            datePickerNextCheckup.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditHealthActivity.this, "Failed to load health data", Toast.LENGTH_SHORT).show());
    }


        private void saveHealthRecord() {
            String healthStatus = editTextHealthStatus.getText().toString().trim();
            String treatment = editTextTreatment.getText().toString().trim();
            String vetName = editTextVetName.getText().toString().trim();
            String vetPhone = editTextVetPhone.getText().toString().trim();
            String selectedLivestock = spinnerLivestock.getSelectedItem().toString();

            if (TextUtils.isEmpty(healthStatus) || TextUtils.isEmpty(treatment) || TextUtils.isEmpty(vetName) || TextUtils.isEmpty(vetPhone)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the Next Checkup Date from DatePicker
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePickerNextCheckup.getYear(), datePickerNextCheckup.getMonth(), datePickerNextCheckup.getDayOfMonth());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nextCheckupDate = sdf.format(calendar.getTime());

            String userId = mAuth.getCurrentUser().getUid();
            Map<String, Object> healthRecord = new HashMap<>();
            healthRecord.put("livestockName", selectedLivestock);
            healthRecord.put("healthStatus", healthStatus);
            healthRecord.put("treatment", treatment);
            healthRecord.put("vetName", vetName);
            healthRecord.put("vetPhone", vetPhone);
            healthRecord.put("nextCheckupDate", nextCheckupDate);
            healthRecord.put("userId", userId);

            if (healthId != null) {
                db.collection("health").document(healthId)
                        .set(healthRecord)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddEditHealthActivity.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(AddEditHealthActivity.this, "Error updating record", Toast.LENGTH_SHORT).show());
            } else {
                db.collection("health")
                        .add(healthRecord)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(AddEditHealthActivity.this, "Record added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(AddEditHealthActivity.this, "Error adding record", Toast.LENGTH_SHORT).show());
            }
        }
    }
