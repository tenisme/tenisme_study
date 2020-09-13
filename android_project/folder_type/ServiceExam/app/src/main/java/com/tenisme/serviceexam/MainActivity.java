package com.tenisme.serviceexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 이 액티비티에서 바인드를 할 서비스의 레퍼런스를 저장할 변수
    private MyService myService;
    private boolean bound; // 바인딩 되어있는지 안 되어있는지를 체크하기 위한 boolean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void stopService(View view) {
        if (bound) {
            myService.onDestroy(); // thread 종료, count 값 초기화
            myService.stopForeground(true); // foreground 해제
        }
//        // 기존 코드
//        Intent intent = new Intent(this, MyService.class);
//        stopService(intent);
    }

    public void onStartIntentService(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

    public void onStartForegroundService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction("startForeground");
        // 실행 코드
        // 오레오 이상을 위한 실행 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            // 일반 실행 코드
            startService(intent);
        }
    }

    public void getCountValue(View view) {
        if (bound) {
            Toast.makeText(this, "카운팅 : " + myService.getCount(), Toast.LENGTH_SHORT).show();
        }
    }

    // 일반적으로 자동으로 bind 하려는 경우에는 onStart 에서 bind 하면 된다.
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MyService.class);
        // 플래그 자리에 놓는 BIND_AUTO_CREATE : 일반적으로 씀. 서비스를 자동으로 생성해주고 바인드까지 해주는 플래그.
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

//    // 액티비티 종료시 바인드 끊어주기
//    // 안 끊어주면 레퍼런스때문에 메모리가 줄줄 샐 수 있음.
//    // 앱이 동작하고 있을 때(전면에 있을 때)만 서비스와 bind 하려는 경우에 사용.
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(bound){
//            unbindService(connection); // 바인드 해제
//            bound = false; // 바인딩 종료 체크
//        }
//    }

    // bind Service 를 위해서 ServiceConnection 객체를 만들어야 함
    private ServiceConnection connection = new ServiceConnection() {
        // 연결되었을 때(바인드 되었을 때)의 콜백
        // MyService.java 의 IBinder onBind(Intent intent)의 iBinder 가 여기로 넘어오게 됨.
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            // 바인더를 통해서 MyService 레퍼런스를 얻음
            myService = myBinder.getService();
            bound = true; // 바인딩 되어있음으로 표시
        }

        // 바운드 연결 해제되었을 때의 콜백
        // 바운드가 끊길 때마다 호출되는 것이 아니라,
        // 예기치 않은 상황에서 안드로이드 OS에 의해서 Kill(강제종료가) 되었을 때 호출되는 콜백임.
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
}