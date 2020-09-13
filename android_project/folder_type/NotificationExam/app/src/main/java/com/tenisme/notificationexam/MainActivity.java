package com.tenisme.notificationexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNotification(View view) {
        show();
    }

    private void show() {
        // notification 을 보여주는 코드를 작성
        // NotificationCompat 은 하위 호환이 되고, 안드로이드 버전별로 분기를 타지 않기 때문에 사용한다.
        // 오레오부터는 채널 id를 꼭 설정해줘야 한다.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알림 제목");
        builder.setContentText("알림 세부 텍스트");

        Intent intent = new Intent(this, MainActivity.class);
        // PendingIntent.FLAG_UPDATE_CURRENT : 이미 알림이 한 번 실행이 되어있으면 다시 호출되었을 때 내용을 업데이트해준다.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // pendingIntent 안에 있는 intent 가 실행된다.
        builder.setContentIntent(pendingIntent);

        // 큰 아이콘을 설정할 때는 위처럼 리소스 아이디를 직접 입력한다고 되지 않고,
        // 아래처럼 비트맵으로 변환을 해준 다음 셋팅해야 한다.
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        // 알림 컬러 지정
        builder.setColor(Color.RED);

        // 알림 사운드 지정 : 기본 알람음을 가져오는 코드
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        // 알림시 진동 설정 : 진동은 long 형태의 배열을 준비. 진동 설정의 규칙.
        // 배열 안에 들어있는 숫자는 밀리세컨(초) 단위.
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);

        // 터치하면 이 인텐트가 실행됨. 노티가 날아가게 할 거냐. true 터치해도 알림이 안 날아가게 하려면 false
        builder.setAutoCancel(true);

        // manager 생성
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // ★ 오레오는 알림매니저에 채널 id를 등록을 해야함.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // builder.build() : builder 가 notification 객체를 return 한다.
        manager.notify(0, builder.build());
    }

    // notification 해제
    public void deleteNotification(View view) {
        hide();
    }

    private void hide() {
        NotificationManagerCompat.from(this).cancel(0);
    }
}