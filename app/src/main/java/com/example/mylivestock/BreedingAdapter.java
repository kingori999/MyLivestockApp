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

public class BreedingAdapter extends RecyclerView.Adapter<BreedingAdapter.BreedingViewHolder> {

    private List<BreedingRecord> breedingList = new ArrayList<>();
    private Context context;



    public BreedingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public BreedingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_breeding, parent, false);
            return new BreedingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BreedingViewHolder holder, int position) {
            BreedingRecord breedingRecord = breedingList.get(position);
            holder.femaleLivestockNameTextView.setText(breedingRecord.getFemaleLivestockName());
            holder.maleLivestockNameTextView.setText(breedingRecord.getMaleLivestockName());
            holder.breedingDateTextView.setText(breedingRecord.getBreedingDate());
            holder.expectedDueDateTextView.setText(breedingRecord.getExpectedDueDate());
            holder.methodTextView.setText(breedingRecord.getMethod());

            holder.editButton.setOnClickListener(v -> {
                if (context instanceof BreedingActivity) {
                    ((BreedingActivity) context).editBreedingRecord(breedingRecord);
                }
            });

            holder.deleteButton.setOnClickListener(v -> {
                if (context instanceof BreedingActivity) {
                    ((BreedingActivity) context).deleteBreedingRecord(breedingRecord);
                }
            });
        }

        @Override
        public int getItemCount() {
            return breedingList.size();
        }

        public void setBreedingList(List<BreedingRecord> breedingList) {
            this.breedingList = breedingList;
            notifyDataSetChanged();
        }

        public static class BreedingViewHolder extends RecyclerView.ViewHolder {

            TextView femaleLivestockNameTextView, maleLivestockNameTextView, breedingDateTextView, expectedDueDateTextView, methodTextView;
            Button editButton, deleteButton;

            public BreedingViewHolder(@NonNull View itemView) {
                super(itemView);
                femaleLivestockNameTextView = itemView.findViewById(R.id.text_view_female_livestock_name);
                maleLivestockNameTextView = itemView.findViewById(R.id.text_view_male_livestock_name);
                breedingDateTextView = itemView.findViewById(R.id.text_view_breeding_date);
                expectedDueDateTextView = itemView.findViewById(R.id.text_view_expected_due_date);
                methodTextView = itemView.findViewById(R.id.text_view_method);
                editButton = itemView.findViewById(R.id.button_edit);
                deleteButton = itemView.findViewById(R.id.button_delete);
            }
        }
    }
