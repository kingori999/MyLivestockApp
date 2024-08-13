package com.example.mylivestock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEditNutritionActivity extends AppCompatActivity {

    private Spinner spinnerAnimalType;
    private EditText editTextFeedType, editTextQuantity;
    private TimePicker timePicker;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private NutritionViewModel nutritionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_nutrition);

        spinnerAnimalType = findViewById(R.id.spinner_animal_type);
        editTextFeedType = findViewById(R.id.edit_text_feed_type);
        editTextQuantity = findViewById(R.id.edit_text_quantity);
        timePicker = findViewById(R.id.time_picker);
        buttonSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        nutritionViewModel = new ViewModelProvider(this).get(NutritionViewModel.class);

        ArrayAdapter<CharSequence> animalTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.animal_type_array, android.R.layout.simple_spinner_item);
        animalTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimalType.setAdapter(animalTypeAdapter);

        buttonSave.setOnClickListener(v -> saveNutritionData());
    }

    private void saveNutritionData() {
        String animalType = spinnerAnimalType.getSelectedItem().toString();
        String feedType = editTextFeedType.getText().toString().trim();
        String quantity = editTextQuantity.getText().toString().trim();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String time = hour + ":" + (minute < 10 ? "0" + minute : minute);

        if (TextUtils.isEmpty(animalType) || TextUtils.isEmpty(feedType) || TextUtils.isEmpty(quantity)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        Nutrition nutrition = new Nutrition(animalType, feedType, quantity, time, userId);
        nutritionViewModel.insert(nutrition);

        finish();
    }
}