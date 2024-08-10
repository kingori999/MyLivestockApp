package com.example.mylivestock;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.HealthViewHolder> {

    private List<Health> healthList = new ArrayList<>();
    private Context context;

    public HealthAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HealthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health, parent, false);
        return new HealthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthViewHolder holder, int position) {
        Health health = healthList.get(position);
        holder.livestockNameTextView.setText("Animal Name: " +health.getLivestockName());
        holder.healthStatusTextView.setText("Health Status: " +health.getHealthStatus());
        holder.treatmentTextView.setText("Treatment given: " +health.getTreatment());
        holder.nextCheckupTextView.setText("Next checkup date: " +health.getNextCheckupDate());

        holder.editButton.setOnClickListener(v -> {
            if (context instanceof HealthActivity) {
                ((HealthActivity) context).editHealthRecord(health);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof HealthActivity) {
                ((HealthActivity) context).deleteHealthRecord(health);
            }
        });
    }

    @Override
    public int getItemCount() {
        return healthList.size();
    }

    public void setHealthList(List<Health> healthList) {
        this.healthList = healthList;
        notifyDataSetChanged();
    }

    public static class HealthViewHolder extends RecyclerView.ViewHolder {

        TextView livestockNameTextView, healthStatusTextView, treatmentTextView, nextCheckupTextView;
        Button editButton, deleteButton;

        public HealthViewHolder(@NonNull View itemView) {
            super(itemView);
            livestockNameTextView = itemView.findViewById(R.id.text_view_livestock_name);
            healthStatusTextView = itemView.findViewById(R.id.text_view_health_status);
            treatmentTextView = itemView.findViewById(R.id.text_view_treatment);
            nextCheckupTextView = itemView.findViewById(R.id.text_view_next_checkup);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
