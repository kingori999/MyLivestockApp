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

public class MilkActivity extends AppCompatActivity {

    private MilkViewModel milkViewModel;
    private MilkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_milk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new MilkAdapter(this);
        recyclerView.setAdapter(adapter);

        milkViewModel = new ViewModelProvider(this).get(MilkViewModel.class);
        milkViewModel.getAllMilkRecords().observe(this, new Observer<List<MilkRecord>>() {
            @Override
            public void onChanged(List<MilkRecord> milkRecords) {
                adapter.setMilkList(milkRecords);
            }
        });

        findViewById(R.id.fab_add_milk_record).setOnClickListener(v -> {
            startActivity(new Intent(MilkActivity.this, AddEditMilkActivity.class));
        });
    }

    public void deleteMilkRecord(MilkRecord milkRecord) {
        milkViewModel.delete(milkRecord);
        Toast.makeText(this, "Milk record deleted", Toast.LENGTH_SHORT).show();
    }

    public void editMilkRecord(MilkRecord milkRecord) {
        Intent intent = new Intent(MilkActivity.this, AddEditMilkActivity.class);
        intent.putExtra("milkId", milkRecord.getId());
        startActivity(intent);
    }
}
