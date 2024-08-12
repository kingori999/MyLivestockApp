package com.example.mylivestock;

import java.util.List;

public class Report {

    private String id;
    private String reportType;
    private String dateRange;
    private String userId;
    private List<String> data; // Stores report content in list form

    public Report() {
        // Default constructor required for Firestore
    }

    public Report(String reportType, String dateRange, String userId, List<String> data) {
        this.reportType = reportType;
        this.dateRange = dateRange;
        this.userId = userId;
        this.data = data;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
