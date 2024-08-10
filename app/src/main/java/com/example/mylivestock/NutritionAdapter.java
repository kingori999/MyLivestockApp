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

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.NutritionViewHolder> {

    private List<Nutrition> nutritionList = new ArrayList<>();
    private Context context;

    public NutritionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NutritionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nutrition, parent, false);
        return new NutritionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NutritionViewHolder holder, int position) {
        Nutrition nutrition = nutritionList.get(position);
        holder.livestockNameTextView.setText("Animal Name: " +nutrition.getLivestockName());
        holder.feedTypeTextView.setText("Feed Type: " +nutrition.getFeedType());
        holder.quantityTextView.setText("Quantity: " +nutrition.getQuantity());
        holder.dateTimeTextView.setText("Date: " +nutrition.getDateTime());

        holder.editButton.setOnClickListener(v -> {
            if (context instanceof NutritionActivity) {
                ((NutritionActivity) context).editNutritionRecord(nutrition);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof NutritionActivity) {
                ((NutritionActivity) context).deleteNutritionRecord(nutrition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nutritionList.size();
    }

    public void setNutritionList(List<Nutrition> nutritionList) {
        this.nutritionList = nutritionList;
        notifyDataSetChanged();
    }

    public static class NutritionViewHolder extends RecyclerView.ViewHolder {

        TextView livestockNameTextView, feedTypeTextView, quantityTextView, dateTimeTextView;
        Button editButton, deleteButton;

        public NutritionViewHolder(@NonNull View itemView) {
            super(itemView);
            livestockNameTextView = itemView.findViewById(R.id.text_view_livestock_name);
            feedTypeTextView = itemView.findViewById(R.id.text_view_feed_type);
            quantityTextView = itemView.findViewById(R.id.text_view_quantity);
            dateTimeTextView = itemView.findViewById(R.id.text_view_date_time);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
