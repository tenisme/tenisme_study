package com.tenisme.trackme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AppSetting extends AppCompatActivity {

    TextView txtTemporary5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        txtTemporary5 = findViewById(R.id.txtTemporary5);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(AppSetting.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}