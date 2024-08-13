package com.example.mylivestock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylivestock.Nutrition;

import java.util.List;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.NutritionHolder> {

    private List<Nutrition> nutritionList;
    private OnItemClickListener listener;
    private Context context;

    public NutritionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NutritionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nutrition, parent, false);
        return new NutritionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NutritionHolder holder, int position) {
        Nutrition currentNutrition = nutritionList.get(position);
        holder.textViewAnimalType.setText(currentNutrition.getAnimalType());
        holder.textViewFeedType.setText(currentNutrition.getFeedType());
        holder.textViewQuantity.setText(currentNutrition.getQuantity());
        holder.textViewDateTime.setText(currentNutrition.getTime());

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onEditClick(currentNutrition);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(currentNutrition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nutritionList == null ? 0 : nutritionList.size();
    }

    public void setNutritionList(List<Nutrition> nutritionList) {
        this.nutritionList = nutritionList;
        notifyDataSetChanged();
    }

    class NutritionHolder extends RecyclerView.ViewHolder {
        private TextView textViewAnimalType;
        private TextView textViewFeedType;
        private TextView textViewQuantity;
        private TextView textViewDateTime;
        private Button buttonEdit;
        private Button buttonDelete;

        public NutritionHolder(@NonNull View itemView) {
            super(itemView);
            textViewAnimalType = itemView.findViewById(R.id.text_view_animal_type);
            textViewFeedType = itemView.findViewById(R.id.text_view_feed_type);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            textViewDateTime = itemView.findViewById(R.id.text_view_date_time);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }

    public interface OnItemClickListener {
        void onEditClick(Nutrition nutrition);
        void onDeleteClick(Nutrition nutrition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}