package com.tenisme.push;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("global")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 푸시를 여기서 처리

                        // 에러 처리
                        if(!task.isSuccessful()){
                            Log.i("Pushee", "Error!");
                            return;
                        }

                        getPushData();
                    }
                });

    }

    private void getPushData() {
        // 푸시가 올 때의 동작 (intent 로 처리)
        if(getIntent().getExtras() != null){
            String name = getIntent().getExtras().getString("name");
            Log.i("Pushee", "name : "+name);
        }
    }
}