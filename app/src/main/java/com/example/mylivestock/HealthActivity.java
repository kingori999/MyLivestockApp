package com.example.mylivestock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HealthActivity extends AppCompatActivity {

    private HealthViewModel healthViewModel;
    private HealthAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_health);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new HealthAdapter(this);
        recyclerView.setAdapter(adapter);

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        healthViewModel.getAllHealthRecords().observe(this, new Observer<List<Health>>() {
            @Override
            public void onChanged(List<Health> healthList) {
                adapter.setHealthList(healthList);
            }
        });

        findViewById(R.id.fab_add_health_record).setOnClickListener(v -> {
            startActivity(new Intent(HealthActivity.this, AddEditHealthActivity.class));
        });
    }

    public void deleteHealthRecord(Health health) {
        healthViewModel.delete(health);
        Toast.makeText(this, "Health record deleted", Toast.LENGTH_SHORT).show();
    }

    public void editHealthRecord(Health health) {
        Intent intent = new Intent(HealthActivity.this, AddEditHealthActivity.class);
        intent.putExtra("healthId", health.getId());
        startActivity(intent);
    }
}
