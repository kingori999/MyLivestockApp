package com.example.mylivestock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylivestock.Nutrition;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    private NutritionViewModel nutritionViewModel;
    private NutritionAdapter adapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        auth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_nutrition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new NutritionAdapter(this);
        recyclerView.setAdapter(adapter);

        nutritionViewModel = new ViewModelProvider(this).get(NutritionViewModel.class);
        nutritionViewModel.getAllNutrition(auth.getCurrentUser().getUid()).observe(this, new Observer<List<Nutrition>>() {
            @Override
            public void onChanged(List<Nutrition> nutritionList) {
                adapter.setNutritionList(nutritionList);
            }
        });

        findViewById(R.id.fab_add_nutrition_record).setOnClickListener(v -> {
            startActivity(new Intent(NutritionActivity.this, AddEditNutritionActivity.class));
        });
    }

    public void deleteNutritionRecord(Nutrition nutrition) {
        nutritionViewModel.delete(nutrition);
        Toast.makeText(this, "Nutrition record deleted", Toast.LENGTH_SHORT).show();
    }

    public void editNutritionRecord(Nutrition nutrition) {
        Intent intent = new Intent(NutritionActivity.this, AddEditNutritionActivity.class);
        intent.putExtra("nutritionId", nutrition.getId());
        startActivity(intent);
    }
}