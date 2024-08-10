package com.example.mylivestock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MilkAdapter extends RecyclerView.Adapter<MilkAdapter.MilkViewHolder> {

    private List<MilkRecord> milkList = new ArrayList<>();
    private Context context;

    public MilkAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MilkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_milk, parent, false);
        return new MilkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MilkViewHolder holder, int position) {
        MilkRecord milkRecord = milkList.get(position);
        holder.livestockNameTextView.setText("Animal Name: " +milkRecord.getLivestockName());
        holder.productionDateTextView.setText("Milking Date: " +milkRecord.getProductionDate());
        holder.sessionTimeTextView.setText("Milking Time: " + milkRecord.getSessionTime());
        holder.quantityTextView.setText(String.valueOf(milkRecord.getQuantity() + "liters"));

        holder.editButton.setOnClickListener(v -> {
            if (context instanceof MilkActivity) {
                ((MilkActivity) context).editMilkRecord(milkRecord);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof MilkActivity) {
                ((MilkActivity) context).deleteMilkRecord(milkRecord);
            }
        });
    }

    @Override
    public int getItemCount() {
        return milkList.size();
    }

    public void setMilkList(List<MilkRecord> milkList) {
        this.milkList = milkList;
        notifyDataSetChanged();
    }

    public static class MilkViewHolder extends RecyclerView.ViewHolder {

        TextView livestockNameTextView, productionDateTextView, sessionTimeTextView, quantityTextView;
        Button editButton, deleteButton;

        public MilkViewHolder(@NonNull View itemView) {
            super(itemView);
            livestockNameTextView = itemView.findViewById(R.id.text_view_livestock_name);
            productionDateTextView = itemView.findViewById(R.id.text_view_production_date);
            sessionTimeTextView = itemView.findViewById(R.id.text_view_session_time);
            quantityTextView = itemView.findViewById(R.id.text_view_quantity);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
