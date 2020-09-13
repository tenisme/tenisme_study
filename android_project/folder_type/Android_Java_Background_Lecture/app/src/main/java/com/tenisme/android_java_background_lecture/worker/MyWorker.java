package com.tenisme.android_java_background_lecture.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tenisme.android_java_background_lecture.R;

public class MyWorker extends Worker {

    private static final String CHANNEL_ID = "WorkManager Long Task";
    private NotificationManager notificationManager;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // 기본적으로 백그라운드에서 동작함.
    @NonNull
    @Override
    public Result doWork() {

        notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // 채널 생성
        createNotificationChannel();

        try {
            int num = 0;
            for (int i = 0; i < 100; i++) {

                num++;

                // 데이터를 중간중간 넘겨줌.
                Data data = new Data.Builder().putInt("progress", num).build();
                setProgressAsync(data);

                showNotification(num);

                // 백그라운드 쓰레드에 슬립을 건다 (0.1초)
                Thread.sleep(100);
            }
        } catch(Exception ignored) {
            return Result.failure();
        }
        return Result.success();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WorkManager Long Task";
            String description = "WorkManager Long Task";
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
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("WorkManager Long Task")
                .setProgress(100, progress, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        // 노티 id는 임의로 만들어도 되지만 다른 알림과 겹치지 않도록 주의한다.
        notificationManager.notify(3, notification);
    }
}
