package com.tenisme.android_java_background_lecture.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.tenisme.android_java_background_lecture.R;
import com.tenisme.android_java_background_lecture.Result;

public class JobService extends JobIntentService {

    private static final String CHANNEL_ID = "Job Channel";
    private NotificationManager notificationManager;

    public static void enqueueWork(@NonNull Context context, @NonNull Intent intent){
        JobIntentService.enqueueWork(context, JobService.class, 1000, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notification channel 생성
        createNotificationChannel();
        try {
            int num = 0;
            for (int i = 0; i < 100; i++) {
                num++;
                showNotification(num);
                // 백그라운드 쓰레드에 슬립을 건다 (0.1초)
                Thread.sleep(100);
            }
        } catch(Exception ignored) {

        }
    }

    // notification 만들기
    // 이거 안 만들면 notify 가 안 뜸.
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Job Channel";
            String description = "Job Channel";
            // 채널쪽 우선순위 설정
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

    // notification 의 진행율을 표시할 것임.
    private void showNotification(int progress) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Job Service")
                .setProgress(100, progress, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification);
    }
}
