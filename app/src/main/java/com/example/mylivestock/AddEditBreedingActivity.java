package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AddEditBreedingActivity extends AppCompatActivity {

    private Spinner spinnerFemaleLivestock, spinnerMaleLivestock, spinnerMethod;
    private DatePicker datePickerBreedingDate;
    private TextView textViewExpectedDueDate;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private BreedingViewModel breedingViewModel;
    private String breedingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_breeding);

        spinnerFemaleLivestock = findViewById(R.id.spinner_female_livestock);
        spinnerMaleLivestock = findViewById(R.id.spinner_male_livestock);
        datePickerBreedingDate = findViewById(R.id.date_picker_breeding_date);
        spinnerMethod = findViewById(R.id.spinner_method);
        textViewExpectedDueDate = findViewById(R.id.text_view_expected_due_date);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        breedingViewModel = new ViewModelProvider(this).get(BreedingViewModel.class);

        loadLivestockIntoSpinners();
        setupBreedingMethodSpinner();

        if (getIntent().hasExtra("breedingId")) {
            breedingId = getIntent().getStringExtra("breedingId");
            loadBreedingData(breedingId);
        }

        datePickerBreedingDate.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                calendar.add(Calendar.DAY_OF_MONTH, 283); // Average gestation period for cows (283 days)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String expectedDueDate = sdf.format(calendar.getTime());
                textViewExpectedDueDate.setText("Expected Due Date: " + expectedDueDate);
            }
        });

        buttonSave.setOnClickListener(v -> saveBreedingRecord());
    }

    private void loadLivestockIntoSpinners() {
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
                    spinnerFemaleLivestock.setAdapter(adapter);
                    spinnerMaleLivestock.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditBreedingActivity.this, "Failed to load livestock", Toast.LENGTH_SHORT).show());
    }

    private void setupBreedingMethodSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.breeding_methods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMethod.setAdapter(adapter);
    }

    private void loadBreedingData(String breedingId) {
        db.collection("breedingRecords").document(breedingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String femaleLivestockName = documentSnapshot.getString("femaleLivestockName");
                        String maleLivestockName = documentSnapshot.getString("maleLivestockName");
                        String breedingDate = documentSnapshot.getString("breedingDate");
                        String method = documentSnapshot.getString("method");
                        String expectedDueDate = documentSnapshot.getString("expectedDueDate");

                        // Populate the fields with existing data
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerFemaleLivestock.getAdapter();
                        int femalePosition = adapter.getPosition(femaleLivestockName);
                        spinnerFemaleLivestock.setSelection(femalePosition);

                        int malePosition = adapter.getPosition(maleLivestockName);
                        spinnerMaleLivestock.setSelection(malePosition);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(sdf.parse(breedingDate));
                            datePickerBreedingDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<CharSequence> methodAdapter = (ArrayAdapter<CharSequence>) spinnerMethod.getAdapter();
                        int methodPosition = methodAdapter.getPosition(method);
                        spinnerMethod.setSelection(methodPosition);

                        textViewExpectedDueDate.setText("Expected Due Date: " + expectedDueDate);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AddEditBreedingActivity.this, "Failed to load breeding data", Toast.LENGTH_SHORT).show());
    }

    private void saveBreedingRecord() {
        String femaleLivestockName = spinnerFemaleLivestock.getSelectedItem().toString();
        String maleLivestockName = spinnerMaleLivestock.getSelectedItem().toString();
        String method = spinnerMethod.getSelectedItem().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePickerBreedingDate.getYear(), datePickerBreedingDate.getMonth(), datePickerBreedingDate.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String breedingDate = sdf.format(calendar.getTime());
        String expectedDueDate = textViewExpectedDueDate.getText().toString().replace("Expected Due Date: ", "");

        if (TextUtils.isEmpty(femaleLivestockName) || TextUtils.isEmpty(maleLivestockName)) {
            Toast.makeText(this, "Please select livestock", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        BreedingRecord breedingRecord = new BreedingRecord(femaleLivestockName, maleLivestockName, breedingDate, expectedDueDate, method, userId);

        if (breedingId != null) {
            breedingRecord.setId(breedingId);
            breedingViewModel.update(breedingRecord);
            Toast.makeText(this, "Breeding record updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            breedingViewModel.insert(breedingRecord);
            Toast.makeText(this, "Breeding record added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
