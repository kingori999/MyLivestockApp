package com.example.mylivestock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userEmailTextView;
    private Button logoutButton, generateReportsButton;
    private FloatingActionButton btnHowToUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userEmailTextView = findViewById(R.id.user_email);
        logoutButton = findViewById(R.id.button_logout);
        generateReportsButton = findViewById(R.id.button_generate_reports);
        GridLayout gridLayout = findViewById(R.id.grid_layout);
        btnHowToUse = findViewById(R.id.btnHowToUse);

        btnHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HowToUse.class));
            }
        });

        if (currentUser != null) {
            userEmailTextView.setText(currentUser.getEmail());
        }

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Set click listeners for grid items
        findViewById(R.id.icon_livestock).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InventoryActivity.class));
        });

        findViewById(R.id.icon_nutrition).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NutritionActivity.class));
        });

        findViewById(R.id.icon_health).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HealthActivity.class));
        });

        findViewById(R.id.icon_milk).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MilkActivity.class));
        });

        findViewById(R.id.icon_breeding).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BreedingActivity.class));
        });

        findViewById(R.id.icon_tasks).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TaskActivity.class));
        });

        // Handle Generate Reports button click
        generateReportsButton.setOnClickListener(v -> {
            // Code to generate reports goes here
          startActivity(new Intent(MainActivity.this, GenerateReportActivity.class));
            // Start the report generation activity or service
        });
    }
}