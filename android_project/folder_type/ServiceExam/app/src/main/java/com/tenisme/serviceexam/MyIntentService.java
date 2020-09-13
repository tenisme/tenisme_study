package com.tenisme.serviceexam;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {

    // name : 스레드 이름
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 여기서 백그라운드에서 실행할 코드 작성
        // IntentService 는 실행 횟수만큼 자동 연장되고 스스로 Destroy 해서 StopService 를 호출하지 않아도 된다.
        // 따로 Thread 를 호출하지 않아도 Thread 를 사용할 수 있다.
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Log.d("MyIntentService", "인텐트 서비스 동작 중 : " + i);
        }
    }
}
