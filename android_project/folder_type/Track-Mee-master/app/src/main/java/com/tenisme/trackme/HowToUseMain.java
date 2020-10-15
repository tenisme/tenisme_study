package com.tenisme.trackme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HowToUseMain extends AppCompatActivity {

    Button btnMain;
    TextView txtTemporary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use_main);

        btnMain = findViewById(R.id.btnMain);
        txtTemporary = findViewById(R.id.txtTemporary);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HowToUseMain.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}