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

public class BreedingActivity extends AppCompatActivity {

    private BreedingViewModel breedingViewModel;
    private BreedingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeding);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_breeding);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new BreedingAdapter(this);
        recyclerView.setAdapter(adapter);

        breedingViewModel = new ViewModelProvider(this).get(BreedingViewModel.class);
        breedingViewModel.getAllBreedingRecords().observe(this, new Observer<List<BreedingRecord>>() {
            @Override
            public void onChanged(List<BreedingRecord> breedingRecords) {
                adapter.setBreedingList(breedingRecords);
            }
        });

        findViewById(R.id.fab_add_breeding_record).setOnClickListener(v -> {
            startActivity(new Intent(BreedingActivity.this, AddEditBreedingActivity.class));
        });
    }

    public void deleteBreedingRecord(BreedingRecord breedingRecord) {
        breedingViewModel.delete(breedingRecord);
        Toast.makeText(this, "Breeding record deleted", Toast.LENGTH_SHORT).show();
    }

    public void editBreedingRecord(BreedingRecord breedingRecord) {
        Intent intent = new Intent(BreedingActivity.this, AddEditBreedingActivity.class);
        intent.putExtra("breedingId", breedingRecord.getId());
        startActivity(intent);
    }
}
