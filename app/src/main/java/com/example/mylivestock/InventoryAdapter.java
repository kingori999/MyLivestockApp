package com.example.mylivestock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder> {

    private List<Livestock> livestockList = new ArrayList<>();
    private InventoryActivity inventoryActivity;

    public InventoryAdapter(InventoryActivity inventoryActivity) {
        this.inventoryActivity = inventoryActivity;
    }

    @NonNull
    @Override
    public InventoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livestock, parent, false);
        return new InventoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryHolder holder, int position) {
        Livestock currentLivestock = livestockList.get(position);
        holder.textViewName.setText("Animal Name: " +currentLivestock.getName());
        holder.textViewType.setText("Type: " +currentLivestock.getType());
        holder.textViewBreed.setText("Breed: " +currentLivestock.getBreed());
        holder.textViewHealthStatus.setText("Health Status: " +currentLivestock.getHealthStatus());

        holder.buttonDelete.setOnClickListener(v -> inventoryActivity.deleteLivestock(currentLivestock));
    }

    @Override
    public int getItemCount() {
        return livestockList.size();
    }

    public void setLivestock(List<Livestock> livestock) {
        this.livestockList = livestock;
        notifyDataSetChanged();
    }

    class InventoryHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewType;
        private TextView textViewBreed;
        private TextView textViewHealthStatus;
        private Button buttonDelete;

        public InventoryHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewType = itemView.findViewById(R.id.text_view_type);
            textViewBreed = itemView.findViewById(R.id.text_view_breed);
            textViewHealthStatus = itemView.findViewById(R.id.text_view_health_status);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
