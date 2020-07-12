package com.tenisme.timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgAlarm;
    TextView txtSecond;
    Button btnSetting;
    Button btnCancel;
    Button btnStart;

    EditText editTitle;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAlarm = findViewById(R.id.imgAlarm);
        txtSecond = findViewById(R.id.txtSecond);
        btnSetting = findViewById(R.id.btnSetting);
        btnCancel = findViewById(R.id.btnCancel);
        btnStart = findViewById(R.id.btnStart);

        imgAlarm.setImageResource(R.drawable.alarm);

        btnSetting.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnStart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnSetting){
            createPopupDialog();
        }
        if(v == btnCancel){

        }
        if(v == btnStart){

        }
    }

    public void createPopupDialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog,null);

        editTitle = alertView.findViewById(R.id.editTitle);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTitle.getText().toString().trim();

                if(title.isEmpty()) {
                    Toast.makeText(MainActivity.this, "초를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                txtSecond.setText(title+"초");
            }
        });
        alert.setNegativeButton("NO",null);

        alert.setView(alertView);
        alert.setCancelable(false);

        dialog = alert.create();
        dialog.show();
    }
}