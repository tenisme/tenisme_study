package com.tenisme.trackme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TimeLog extends AppCompatActivity {

    TextView txtTemporary4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_log);

        txtTemporary4 = findViewById(R.id.txtTemporary4);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(TimeLog.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}