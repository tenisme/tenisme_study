package com.tenisme.badthreadexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

// Thread 사용의 안 좋은 예시를 보여주기 위한 프로젝트
public class MainActivity extends AppCompatActivity {

    private Thread thread;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startThread(View view) {
        if(thread == null) {
            thread = new Thread("My Thread") {
                @Override
                public void run() {
                    for(int i = 0; i < 100; i++) {
                        try {
                            count++;
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        Log.d("MyThread", "스레드 동작 중 : " + count);
                    }
                }
            };
            thread.start();
        }
    }
    
    public void stopThread(View view) {
        if(thread != null) {
            thread.interrupt();
            thread = null;
            count = 0;
        }
    }
}