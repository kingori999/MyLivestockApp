package com.example.mylivestock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private LivestockViewModel livestockViewModel;
    private InventoryAdapter adapter;
    private FloatingActionButton fabAddLivestock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewInventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //declaration of floating button
        fabAddLivestock = findViewById(R.id.fabAddLivestock);

        //on click listener for floating button
        fabAddLivestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryActivity.this, AddEditLivestockActivity.class));
            }
        });

        adapter = new InventoryAdapter(this);
        recyclerView.setAdapter(adapter);

        livestockViewModel = new ViewModelProvider(this).get(LivestockViewModel.class);
        livestockViewModel.getAllLivestock().observe(this, new Observer<List<Livestock>>() {
            @Override
            public void onChanged(List<Livestock> livestock) {
                adapter.setLivestock(livestock);
            }
        });

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterLivestock(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLivestock(newText);
                return false;
            }
        });
    }

    private void filterLivestock(String query) {
        livestockViewModel.searchLivestock(query).observe(this, new Observer<List<Livestock>>() {
            @Override
            public void onChanged(List<Livestock> livestock) {
                adapter.setLivestock(livestock);
            }
        });
    }

    public void deleteLivestock(Livestock livestock) {
        livestockViewModel.delete(livestock);
        Toast.makeText(this, "Livestock deleted", Toast.LENGTH_SHORT).show();
    }
}
