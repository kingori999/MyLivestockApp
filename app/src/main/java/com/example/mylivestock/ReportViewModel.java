/**
 * @Author: kingori999 kingxly17@gmail.com
 * @Date: 2024-08-12 12:39:01
 * @LastEditors: kingori999 kingxly17@gmail.com
 * @LastEditTime: 2024-08-12 12:40:40
 * @FilePath: app/src/main/java/com/example/mylivestock/ReportViewModel.java
 * @Description: 这是默认设置,可以在设置》工具》File Description中进行配置
 */
package com.example.mylivestock;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private ReportRepository repository;
    private LiveData<List<Report>> allReports;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        repository = new ReportRepository(application);
        allReports = repository.getAllReports();
    }

    public void insert(Report report) {
        repository.insert(report);
    }

    public void update(Report report) {
        repository.update(report);
    }

    public void delete(Report report) {
        repository.delete(report);
    }

    public LiveData<List<Report>> getAllReports() {
        return allReports;
    }
}
