/**
 * @Author: 
 * @Date: 2024-08-08 22:26:58
 * @LastEditors: 
 * @LastEditTime: 2024-08-08 22:27:37
 * @FilePath: app/src/androidTest/java/com/example/mylivestock/ExampleInstrumentedTest.java
 * @Description: 这是默认设置,可以在设置》工具》File Description中进行配置
 */
package com.example.mylivestock;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mylivestock", appContext.getPackageName());
    }
}