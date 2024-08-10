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

public class TaskActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> taskList) {
                adapter.setTaskList(taskList);
            }
        });

        findViewById(R.id.fab_add_task).setOnClickListener(v -> {
            startActivity(new Intent(TaskActivity.this, AddEditTaskActivity.class));
        });
    }

    public void deleteTask(Task task) {
        taskViewModel.delete(task);
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    public void editTask(Task task) {
        Intent intent = new Intent(TaskActivity.this, AddEditTaskActivity.class);
        intent.putExtra("taskId", task.getId());
        startActivity(intent);
    }
}