package com.example.mylivestock;

public class Task {

    private String id;
    private String taskTitle;
    private String taskDescription;
    private String deadline;
    private String userId;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Task(String taskTitle, String taskDescription, String deadline, String userId) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.userId = userId;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
