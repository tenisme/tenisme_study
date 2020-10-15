package com.tenisme.trackme.function;

import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenisme.trackme.data.DatabaseHandler;
import com.tenisme.trackme.model.TimeRecord;
import com.tenisme.trackme.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

// 활동별 스탑워치 시작/종료 기능을 구현하는 클래스
public class Record {

    LinearLayout parentLayoutActivity;
    TimeRecord timeRecord;

    Handler handler;
    Runnable runnable;
    Timer timer;

    String log = Utils.LOG;

    public Record() {
    }

    public Record(LinearLayout parentLayoutActivity, TimeRecord timeRecord) {
        this.parentLayoutActivity = parentLayoutActivity;
        this.timeRecord = timeRecord;
    }

    public void startOrStopTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                // 현재시간 가져옴
                long now = System.currentTimeMillis();
                // 현재시간 - 기록 시작 시간 계산
                long timeDiff = now - timeRecord.getStart_time();
                // 밀리초를 Date 타입으로 변경
                Date date = new Date(timeDiff);

                // 시간 포맷 설정
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                // 설정한 시간 포맷에 date 배치
                String formattedTimeDiff = sdf.format(date);
                // 포맷을 변경한 date 를 레이아웃에 표기
                ((TextView) parentLayoutActivity.getChildAt(1)).setText(formattedTimeDiff);
            }
        };

        if (timer != null) {
            handler.removeCallbacks(runnable);
            timer.cancel();
            timer.purge();
            timer = null;
        } else {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, 0, 1000);
        }
    }

}
