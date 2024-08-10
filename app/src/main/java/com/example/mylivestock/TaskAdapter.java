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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList = new ArrayList<>();
    private Context context;

    public TaskAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitleTextView.setText(task.getTaskTitle());
        holder.taskDescriptionTextView.setText(task.getTaskDescription());
        holder.deadlineTextView.setText(task.getDeadline());

        holder.editButton.setOnClickListener(v -> {
            if (context instanceof TaskActivity) {
                ((TaskActivity) context).editTask(task);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof TaskActivity) {
                ((TaskActivity) context).deleteTask(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskTitleTextView, taskDescriptionTextView, deadlineTextView;
        Button editButton, deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.text_view_title);
            taskDescriptionTextView = itemView.findViewById(R.id.text_view_description);
            deadlineTextView = itemView.findViewById(R.id.text_view_deadline);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}