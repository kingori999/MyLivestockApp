package com.example.mylivestock;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button manageUsersButton, viewReportsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        manageUsersButton = findViewById(R.id.manageUsersButton);
        viewReportsButton = findViewById(R.id.viewReportsButton);

        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to manage users
            }
        });

        viewReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to view reports
            }
        });
    }
}
