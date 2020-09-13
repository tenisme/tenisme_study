package com.tenisme.serviceexam;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    private Thread thread;
    private int count = 0;

    // Binder 클래스를 상속받아서 나만의 Binder Class 를 만들 수 있다.
    // 이 IBinder 를 리턴하게 된다는 것은, 결국 Binder 를 상속한 MyService(커스텀 Service) 자기자신의 레퍼런스를 리턴하게 된다는 것.
    private IBinder iBinder = new MyBinder();
    public class MyBinder extends Binder {
        // 외부에서 호출하면 MyService 레퍼런스를 돌려주는 함수
        public MyService getService(){
            return MyService.this;
        }
    }

    public MyService() {

    }

    // startService(intent)하면 실행되는 코드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 여기서 백그라운드에서 실행할 코드 작성

        if ("startForeground".equals(intent.getAction())) {
            startForegroundService();
        } else if (thread == null) {
            thread = new Thread("My Thread") {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        try {
                            count++;
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        Log.d("MyService", "서비스 동작 중 : " + count);
                    }
                }
            };
            thread.start();
        }
        return START_STICKY;
        // START_STICKY : 서비스가 (강제) 중단이 되는 경우에도 서비스가 자동으로 재시작을 하고,
        // 마지막에 전달된 intent 는 다시 전달되지 않는다. 사용예 : 영상 출력 앱
    }

    // stopService(intent)하면 실행되는 코드
    // 메인 액티비티에서 onStop() 코드를 적용시키면 이거는 안 먹힘.
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        // 여기서 스레드 정지하는 코드를 작성
        if (thread != null) {
            thread.interrupt();
            thread = null;
            count = 0;
        }
    }

    // '일반' 서비스에 기본적으로 override 되어있다.
    // 서비스와 메인액티비티는 이 함수에서 return 해주는 IBinder 로 연결되어있다.
    @Override
    public IBinder onBind(Intent intent) {
        // 직접 만든 iBinder 를 return 해서 서로 레퍼런스를 가지게 된다.
        // 상대방 객체 안에 있는 public 메소드를 실행할 수 있다.
        return iBinder;
    }

    // MainActivity 와 바인딩이 되면 Service 에 있는 메소드를 MainActivity 에서 가져올 수 있는지
    // 아래의 메소드로 실행해보자
    public int getCount(){
        return count;
    }

    // foreground 사용을 위한 메소드
    private void startForegroundService() {

        // 반드시 notification 이 필요함.
        // 오레오에서는 채널 id가 반드시 필요함.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행중");

        // 인텐트를 잠시 대기시킨다. 바로 일반 인텐트를 실행시킬 수 없음.
        // PendingIntent 를 지정해서 notification 에 지정해놓으면 이것이 실행된다.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        // 알림 실행 코드는 한 번만 실행되도 되기 때문에 액티비티에서 수행하거나 이 클래스의 생성자에서 수행해도 됨.
        // 오레오 이상의 버전을 위한 알림 실행 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            // IMPORTANCE_~ 에 따라서 알림이 보이는 형태가 조금씩 달라짐.
        }

        // 알림 실행
        startForeground(1, builder.build());

    }
}
