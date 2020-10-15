package com.tenisme.trackme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tenisme.trackme.data.DatabaseHandler;
import com.tenisme.trackme.model.Sentence;
import com.tenisme.trackme.util.Utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Loading extends AppCompatActivity {

    TextView txtSentence;
    TextView txtLoading;

    DatabaseHandler dh;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TimerTask second;
    Handler handler = new Handler();

    ArrayList<Sentence> sentenceArrayList = new ArrayList<>();
    String log = Utils.LOG;
    String loading;
    String dot = ".";
    int cnt = 0;

    Button btnSetLoadingSentence;
    Button btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.i(log, "start on create");

        runningLoading();

        addDefaultSentence();

        btnSetLoadingSentence = findViewById(R.id.btnSetLoadingSentence);
        btnMain = findViewById(R.id.btnHowToOrMain);

        btnSetLoadingSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Loading.this, SetLoadingSentence.class);
                startActivity(i);
                finish();
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getInt("sys_run_app_first", -1) == 1){
                    Intent i = new Intent(Loading.this, HowToUseMain.class);
                    setSharedData_sys("sys_run_app_first", 2);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(Loading.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(log, "start on Resume");

        // DB에 저장된 문구들을 가져와서 로딩 액티비티에 배치하는 작업
        txtSentence = findViewById(R.id.txtSentence);
//        Log.i(log, "setting txtSentence on Activity");

        dh = new DatabaseHandler(Loading.this);
        sentenceArrayList = dh.getAllSentences();
//        Log.i(log, "success get all sentences");

        // 문구 하나만 가져오기
//        sentence.setText(sentenceArrayList.get(0).getSentence());

        // 문구 랜덤으로 가져오기
        int randomIndex = (int) (Math.random() * sentenceArrayList.size());
        txtSentence.setText(sentenceArrayList.get(randomIndex).getSentence());
//        Log.i(log, "set text on txtSentence");
    }

    public void runningLoading() {

        txtLoading = findViewById(R.id.txtLoading);
//        Log.i(log, "setting txtLoading on Activity");

        loading = getString(R.string.loading);
        txtLoading.setText(loading);
//        Log.i(log, "set text on txtLoading");

        second = new TimerTask() {
            @Override
            public void run() {
                UpdateDot();
                cnt++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 300);
    }

    private void UpdateDot() {
        Runnable updater = new Runnable() {
            @Override
            public void run() {

                loading = loading + dot;
                txtLoading.setText(loading);
//                Log.i(log, "set text on txtLoading with dot");

                if (cnt % 9 == 0) {
                    loading = getString(R.string.loading);
                    txtLoading.setText(loading);
//                    Log.i(log, "reset text default on Activity");
                }
            }
        };
        handler.post(updater);
    }

    public void addDefaultSentence(){
        // 앱 첫 시작시 기본 로딩 문구 5개를 등록하기위한 작업
        sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        int runAppFirst = sp.getInt("sys_run_app_first", 0);

        if (runAppFirst == 0) {
            dh = new DatabaseHandler(Loading.this);
            Resources res = getResources();
            String[] default_sentence_array = res.getStringArray(R.array.default_sentence_array);
//            Log.i(log, "loading default sentence array");

            for(String getDefaultSentence : default_sentence_array){
                Sentence forAddSentence = new Sentence(getDefaultSentence);
                dh.addSentence(forAddSentence);
            }
//            Log.i(log, "end of for-loop");

            // 기본 문구 등록 후 run_app_first 를 2로 변경,
            // 앱 재실행시 더이상 기본 문구가 추가로 등록되지 않도록 한다.
            setSharedData_sys("sys_run_app_first", 1);
        }
    }

    public void setSharedData_sys(String key, int value){
        editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}