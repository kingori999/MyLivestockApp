package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditMilkActivity extends AppCompatActivity {

    private Spinner spinnerLivestock;
    private DatePicker datePickerProductionDate;
    private TimePicker timePickerSession;
    private EditText editTextQuantity;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MilkViewModel milkViewModel;
    private String milkId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_milk);

        spinnerLivestock = findViewById(R.id.spinner_livestock);
        datePickerProductionDate = findViewById(R.id.date_picker_production_date);
        timePickerSession = findViewById(R.id.time_picker_session);
        editTextQuantity = findViewById(R.id.edit_text_quantity);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        milkViewModel = new ViewModelProvider(this).get(MilkViewModel.class);

        // Load livestock into spinner
        loadLivestockIntoSpinner();

        if (getIntent().hasExtra("milkId")) {
            milkId = getIntent().getStringExtra("milkId");
            loadMilkData(milkId);
        }

        buttonSave.setOnClickListener(v -> saveMilkRecord());
    }

    private void loadLivestockIntoSpinner() {
        String userId = mAuth.getCurrentUser().getUid(); // Get the logged-in user's ID

        db.collection("livestock")
                .whereEqualTo("userId", userId) // Filter by userId
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
                .addOnFailureListener(e -> Toast.makeText(AddEditMilkActivity.this, "Failed to load livestock", Toast.LENGTH_SHORT).show());
    }

    private void loadMilkData(String milkId) {
        db.collection("milkRecords").document(milkId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String livestockName = documentSnapshot.getString("livestockName");
                        String quantity = documentSnapshot.getString("quantity");
                        String productionDate = documentSnapshot.getString("productionDate");
                        String sessionTime = documentSnapshot.getString("sessionTime");

                        // Populate the fields with existing data
                        editTextQuantity.setText(quantity);


                        // Set livestock spinner to correct value
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerLivestock.getAdapter();
                        int position = adapter.getPosition(livestockName);
                        spinnerLivestock.setSelection(position);

                        // Parse and set the Production Date and Session Time
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(sdf.parse(productionDate + " " + sessionTime));
                            datePickerProductionDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            timePickerSession.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                            timePickerSession.setCurrentMinute(calendar.get(Calendar.MINUTE));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditMilkActivity.this, "Failed to load milk data", Toast.LENGTH_SHORT).show());
    }

    private void saveMilkRecord() {
        String livestockName = spinnerLivestock.getSelectedItem().toString();
        String quantityStr = editTextQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Please enter the quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity = Double.parseDouble(quantityStr);

        // Get the Production Date and Session Time
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePickerProductionDate.getYear(), datePickerProductionDate.getMonth(), datePickerProductionDate.getDayOfMonth(),
                timePickerSession.getCurrentHour(), timePickerSession.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String productionDate = sdf.format(calendar.getTime()).split(" ")[0];
        String sessionTime = sdf.format(calendar.getTime()).split(" ")[1];

        String userId = mAuth.getCurrentUser().getUid();
        MilkRecord milkRecord = new MilkRecord(livestockName, productionDate, sessionTime, quantity, userId);

        if (milkId != null) {
            milkRecord.setId(milkId);
            milkViewModel.update(milkRecord);
            Toast.makeText(this, "Milk record updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            milkViewModel.insert(milkRecord);
            Toast.makeText(this, "Milk record added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
