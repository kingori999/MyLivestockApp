package com.example.mylivestock;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditLivestockActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.mylivestock.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.mylivestock.EXTRA_NAME";
    public static final String EXTRA_TYPE = "com.example.mylivestock.EXTRA_TYPE";
    public static final String EXTRA_HEALTH_STATUS = "com.example.mylivestock.EXTRA_HEALTH_STATUS";


    private EditText editTextName, editTextDateOfBirthOrPurchase;
    private Spinner spinnerType, spinnerBreed, spinnerGender, spinnerBirthOrPurchase, spinnerHealthStatus;
    private LivestockViewModel livestockViewModel;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<String> breedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_livestock);

        editTextName = findViewById(R.id.edit_text_name);
        spinnerType = findViewById(R.id.spinner_type);
        spinnerBreed = findViewById(R.id.spinner_breed);
        spinnerGender = findViewById(R.id.spinner_gender);
        spinnerBirthOrPurchase = findViewById(R.id.spinner_birth_or_purchase);
        editTextDateOfBirthOrPurchase = findViewById(R.id.edit_text_date_of_birth_or_purchase);
        spinnerHealthStatus = findViewById(R.id.spinner_health_status);
        Button buttonSave = findViewById(R.id.button_save);
        Button buttonAddBreed = findViewById(R.id.button_add_breed);

        breedList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.animal_type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> healthStatusAdapter = ArrayAdapter.createFromResource(this,
                R.array.health_status_array, android.R.layout.simple_spinner_item);
        healthStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHealthStatus.setAdapter(healthStatusAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> birthOrPurchaseAdapter = ArrayAdapter.createFromResource(this,
                R.array.birth_or_purchase_array, android.R.layout.simple_spinner_item);
        birthOrPurchaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBirthOrPurchase.setAdapter(birthOrPurchaseAdapter);

        livestockViewModel = new ViewModelProvider(this).get(LivestockViewModel.class);

        final String id = getIntent().getStringExtra(EXTRA_ID);
        editTextName.setText(getIntent().getStringExtra(EXTRA_NAME));
        spinnerType.setSelection(getIndex(spinnerType, getIntent().getStringExtra(EXTRA_TYPE)));
        spinnerHealthStatus.setSelection(getIndex(spinnerHealthStatus, getIntent().getStringExtra(EXTRA_HEALTH_STATUS)));

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadBreedsForAnimalType(spinnerType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        setupDateOfBirthOrPurchasePicker();

        buttonSave.setOnClickListener(v -> saveLivestock(id));

        buttonAddBreed.setOnClickListener(v -> {
            AddBreedDialog dialog = new AddBreedDialog(spinnerType.getSelectedItem().toString());
            dialog.show(getSupportFragmentManager(), "AddBreedDialog");
        });
    }

    private void setupDateOfBirthOrPurchasePicker() {
        editTextDateOfBirthOrPurchase.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddEditLivestockActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> editTextDateOfBirthOrPurchase.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void loadBreedsForAnimalType(String animalType) {
        db.collection("breeds").whereEqualTo("animalType", animalType).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    breedList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        breedList.add(doc.getString("breedName"));
                    }
                    ArrayAdapter<String> breedAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, breedList);
                    breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBreed.setAdapter(breedAdapter);
                });
    }

    private void saveLivestock(String id) {
        String name = editTextName.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String breed = spinnerBreed.getSelectedItem().toString();
        String healthStatus = spinnerHealthStatus.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String birthOrPurchaseStatus = spinnerBirthOrPurchase.getSelectedItem().toString();
        String birthOrPurchaseDate = editTextDateOfBirthOrPurchase.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) || TextUtils.isEmpty(breed) || TextUtils.isEmpty(healthStatus)
                || TextUtils.isEmpty(gender) || TextUtils.isEmpty(birthOrPurchaseStatus) || TextUtils.isEmpty(birthOrPurchaseDate)) {
            // Handle error (e.g., show a Toast)
            return;
        }

        // Get the logged-in user's ID
        String userId = auth.getCurrentUser().getUid();

        Livestock livestock = new Livestock(name, type, breed, healthStatus, userId, gender, birthOrPurchaseStatus, birthOrPurchaseDate);

        if (id != null) {
            livestock.setId(id);
            livestockViewModel.update(livestock);
        } else {
            livestockViewModel.insert(livestock);
        }

        finish();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }
}