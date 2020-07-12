package com.block.showguess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editGuess;
    Button btnGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editGuess = findViewById(R.id.editGuess);
        btnGuess = findViewById(R.id.btnGuess);

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guess = editGuess.getText().toString().trim();
                Log.i("ShowGuess", "trim 없는 경우" + guess);

                Intent i = new Intent(MainActivity.this, ShowGuess.class);
                i.putExtra("key", guess);
                i.putExtra("number", 100);
                i.putExtra("str", "Hello");
                i.putExtra("boo", false);
                startActivity(i);
            }
        });

    }
}