package com.tenisme.trackme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SetButton extends AppCompatActivity {

    TextView txtTemporary3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_button);

        txtTemporary3 = findViewById(R.id.txtTemporary3);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(SetButton.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}