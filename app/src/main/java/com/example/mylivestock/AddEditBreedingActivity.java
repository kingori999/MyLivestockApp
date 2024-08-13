package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditBreedingActivity extends AppCompatActivity {

    private Spinner spinnerFemaleLivestock;
    private Spinner spinnerMaleLivestock;
    private Spinner spinnerMethod;
    private DatePicker datePickerBreedingDate;
    private Button buttonSave;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private BreedingViewModel breedingViewModel;
    private List<String> femaleLivestockList;
    private List<String> maleLivestockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_breeding);

        spinnerFemaleLivestock = findViewById(R.id.spinner_female_livestock);
        spinnerMaleLivestock = findViewById(R.id.spinner_male_livestock);
        spinnerMethod = findViewById(R.id.spinner_method);
        datePickerBreedingDate = findViewById(R.id.date_picker_breeding_date);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        breedingViewModel = new ViewModelProvider(this).get(BreedingViewModel.class);

        femaleLivestockList = new ArrayList<>();
        maleLivestockList = new ArrayList<>();

        loadFemaleLivestock();
        loadBreedingMethods();

        spinnerMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerMethod.getSelectedItem().toString().equals("Natural")) {
                    spinnerMaleLivestock.setVisibility(View.VISIBLE);
                    loadMaleLivestock();
                } else {
                    spinnerMaleLivestock.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        buttonSave.setOnClickListener(v -> saveBreedingRecord());
    }

    private void loadFemaleLivestock() {
        db.collection("livestock")
                .whereEqualTo("gender", "Female")
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    femaleLivestockList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        femaleLivestockList.add(doc.getString("name"));
                    }
                    ArrayAdapter<String> femaleAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, femaleLivestockList);
                    femaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFemaleLivestock.setAdapter(femaleAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load female livestock: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadMaleLivestock() {
        db.collection("livestock")
                .whereEqualTo("gender", "Male")
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    maleLivestockList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        maleLivestockList.add(doc.getString("name"));
                    }
                    ArrayAdapter<String> maleAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, maleLivestockList);
                    maleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMaleLivestock.setAdapter(maleAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load male livestock: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadBreedingMethods() {
        ArrayAdapter<CharSequence> methodAdapter = ArrayAdapter.createFromResource(this,
                R.array.breeding_methods_array, android.R.layout.simple_spinner_item);
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMethod.setAdapter(methodAdapter);
    }

    private void saveBreedingRecord() {
        String femaleLivestockName = spinnerFemaleLivestock.getSelectedItem().toString();
        String breedingMethod = spinnerMethod.getSelectedItem().toString();
        String maleLivestockName = breedingMethod.equals("Natural") ? spinnerMaleLivestock.getSelectedItem().toString() : "N/A";

        if (TextUtils.isEmpty(femaleLivestockName) || TextUtils.isEmpty(breedingMethod)) {
            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current date from the DatePicker
        int day = datePickerBreedingDate.getDayOfMonth();
        int month = datePickerBreedingDate.getMonth();
        int year = datePickerBreedingDate.getYear();
        Calendar breedingDateCalendar = Calendar.getInstance();
        breedingDateCalendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String breedingDate = dateFormat.format(breedingDateCalendar.getTime());

        // The due date calculation section is hidden for now
        /*
        getAnimalType(femaleLivestockName, animalType -> {
            if (animalType != null) {
                int gestationPeriod = getGestationPeriod(animalType);
                if (gestationPeriod > 0) {
                    Calendar dueDateCalendar = (Calendar) breedingDateCalendar.clone();
                    dueDateCalendar.add(Calendar.DAY_OF_YEAR, gestationPeriod);
                    String expectedDueDate = dateFormat.format(dueDateCalendar.getTime());

                    // Get the logged-in user's ID
                    String userId = auth.getCurrentUser().getUid();

                    BreedingRecord breedingRecord = new BreedingRecord(femaleLivestockName, maleLivestockName, breedingDate, expectedDueDate, breedingMethod, userId);

                    breedingViewModel.insert(breedingRecord);
                    finish();
                } else {
                    Toast.makeText(AddEditBreedingActivity.this, "Unable to calculate the due date for the selected animal type.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddEditBreedingActivity.this, "Failed to determine the animal type", Toast.LENGTH_SHORT).show();
            }
        });
        */

        // Save the basic breeding info without calculating due date
        String userId = auth.getCurrentUser().getUid();
        BreedingRecord breedingRecord = new BreedingRecord(femaleLivestockName, maleLivestockName, breedingDate, "N/A", breedingMethod, userId);
        breedingViewModel.insert(breedingRecord);
        finish();
    }

    // You may also comment out or remove the getAnimalType method if not in use
    /*
    private void getAnimalType(String livestockName, AnimalTypeCallback callback) {
        db.collection("livestock")
                .whereEqualTo("name", livestockName)
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String animalType = documentSnapshot.getString("type");
                        callback.onCallback(animalType);
                    } else {
                        callback.onCallback(null); // No result found
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddEditBreedingActivity.this, "Error fetching animal type: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onCallback(null); // In case of error
                });
    }

    private int getGestationPeriod(String animalType) {
        switch (animalType) {
            case "Cow":
                return 279;
            case "Pig":
                return 114;
            case "Goat":
                return 150;
            case "Sheep":
                return 152;
            case "Horse":
                return 336;
            default:
                return 0; // Default value, handle this case separately
        }
    }
    */

    // You can also comment out or remove the AnimalTypeCallback interface if not used
    /*
    public interface AnimalTypeCallback {
        void onCallback(String animalType);
    }
    */
}