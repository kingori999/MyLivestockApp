/**
 * @Author: kingori999 kingxly17@gmail.com
 * @Date: 2024-08-12 14:27:38
 * @LastEditors: kingori999 kingxly17@gmail.com
 * @LastEditTime: 2024-08-12 14:46:49
 * @FilePath: app/src/main/java/com/example/mylivestock/HowToUse.java
 * @Description: 这是默认设置,可以在设置》工具》File Description中进行配置
 */
package com.example.mylivestock;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HowToUse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_how_to_use);
        
    }
}