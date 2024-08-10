package com.example.mylivestock;

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
import java.util.List;

public class AddEditLivestockActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.livestockmanagement.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.livestockmanagement.EXTRA_NAME";
    public static final String EXTRA_TYPE = "com.example.livestockmanagement.EXTRA_TYPE";
    public static final String EXTRA_BREED = "com.example.livestockmanagement.EXTRA_BREED";
    public static final String EXTRA_HEALTH_STATUS = "com.example.livestockmanagement.EXTRA_HEALTH_STATUS";

    private EditText editTextName;
    private Spinner spinnerType;
    private Spinner spinnerBreed;
    private Spinner spinnerHealthStatus;
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

        buttonSave.setOnClickListener(v -> saveLivestock(id));

        buttonAddBreed.setOnClickListener(v -> {
            AddBreedDialog dialog = new AddBreedDialog(spinnerType.getSelectedItem().toString());
            dialog.show(getSupportFragmentManager(), "AddBreedDialog");
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

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) || TextUtils.isEmpty(breed) || TextUtils.isEmpty(healthStatus)) {
            return;
        }

        // Get the logged-in user's ID
        String userId = auth.getCurrentUser().getUid();

        Livestock livestock = new Livestock(name, type, breed, healthStatus, userId);

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