package com.tenisme.trackme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tenisme.trackme.data.DatabaseHandler;
import com.tenisme.trackme.function.Record;
import com.tenisme.trackme.model.Activity;
import com.tenisme.trackme.model.ButtonSeat;
import com.tenisme.trackme.model.TimeRecord;
import com.tenisme.trackme.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TableLayout tableLayout;
    LinearLayout parentLayoutActivity;
    Button btnSetButton;
    Button btnTimeLog;
    Button btnAppSetting;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    DatabaseHandler dh;
    Intent i;
    ButtonSeat buttonSeat;
    TimeRecord timeRecord;
    Record record;

    String log = Utils.LOG;
    int count = 1;
    int button_seat_id;
    int time_record_id;

    TextView txtTemporary2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dh = new DatabaseHandler(MainActivity.this);
        sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);

        txtTemporary2 = findViewById(R.id.txtTemporary2);

        tableLayout = findViewById(R.id.tableLayout);
        btnSetButton = findViewById(R.id.btnSetButton);
        btnTimeLog = findViewById(R.id.btnTimeLog);
        btnAppSetting = findViewById(R.id.btnAppSetting);

        if (sp.getInt("sys_run_app_first", -1) == 2) {
            setDefaultActivity();
            setSharedData_sys("sys_run_app_first", 3);
        }
        setLayout();

        btnSetButton.setOnClickListener(this);
        btnTimeLog.setOnClickListener(this);
        btnAppSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnSetButton) {
            i = new Intent(MainActivity.this, SetButton.class);
            startActivity(i);
            finish();
        }
        if (view == btnTimeLog) {
            i = new Intent(MainActivity.this, TimeLog.class);
            startActivity(i);
            finish();
        }
        if (view == btnAppSetting) {
            i = new Intent(MainActivity.this, AppSetting.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(MainActivity.this, Loading.class);
        startActivity(i);
        finish();
    }

    public void clickActive(View v) {
        parentLayoutActivity = (LinearLayout) v;
        String viewName = getResources().getResourceEntryName(parentLayoutActivity.getId());
        Log.i(log, "Touch " + viewName);

        // 이 자리(레이아웃)의 정보를 button_seat 테이블에서 가져옴
        buttonSeat = dh.getButtonSeatByLayoutId(parentLayoutActivity.getId());
        // 이 레이아웃 id와 연결된(이 레이아웃에 배치된) activity_id를 가져옴
        int activity_id = buttonSeat.getActivity_id();

        // 시작/종료 시간 가져오기 : '종료시간-시작시간' 할 때 필요함(time_record 테이블에 저장)
        long clickedTime = System.currentTimeMillis();

        if(buttonSeat.getTime_record_id() == null){
            // 이 buttonSeat 에 저장된 time_record_id가 없으면 기록 시작

            // 레이아웃 셋팅 변경
            layoutWeightChange(1f);

            // activity_id, 기록 시작/종료 시간을 timeRecord 에 셋팅
            timeRecord = new TimeRecord(activity_id, clickedTime, clickedTime);
            // 셋팅한 timeRecord 를 DB에 저장하고 insert id 값을 뽑아냄.
            time_record_id = (int) dh.addTimeRecord(timeRecord, true);
            // 이 buttonSeat 의 time_record_id를 뽑아온 id로 변경
            buttonSeat.setTime_record_id(time_record_id);
        } else {
            // 이 buttonSeat 에 저장된 time_record_id가 있으면 기록 종료

            // 레이아웃 셋팅 변경
            layoutWeightChange(0f);

            // button_seat 테이블에서 이 자리(로우)에 저장된 time_record_id를 불러옴
            time_record_id = buttonSeat.getTime_record_id();
            // 가져온 id로 time_record 테이블에 저장된 시간 기록(timeRecord)을 불러옴
            timeRecord = dh.getTimeRecord(time_record_id);
            // 가져온 timeRecord 의 종료 시간을 변경
            timeRecord.setFinish_time(clickedTime);
            // 종료 시간을 time_record 테이블에 업데이트
            dh.addTimeRecord(timeRecord, false);
            // 이 buttonSeat 의 time_record_id를 null 로 변경
            buttonSeat.setTime_record_id(null);
        }
        // 해당 buttonSeat 의 변경 사항을 button_seat 테이블에 업데이트
        dh.updateButtonSeat(buttonSeat);

        // 타이머 시작/종료
        record = new Record(parentLayoutActivity, timeRecord);
        record.startOrStopTimer();

//        // buttonSeat Log
//        if(buttonSeat.getTime_record_id() == null){
//            Log.i(log, "button_seat_id : " + buttonSeat.getButton_seat_id()
//                    + ", layout_id : " + buttonSeat.getLayout_id()
//                    + ", activity_id : " + buttonSeat.getActivity_id()
//                    + ", time_record_id : null");
//        }else{
//            Log.i(log, "button_seat_id : " + buttonSeat.getButton_seat_id()
//                    + ", layout_id : " + buttonSeat.getLayout_id()
//                    + ", activity_id : " + buttonSeat.getActivity_id()
//                    + ", time_record_id : " + buttonSeat.getTime_record_id().toString());
//        }
//
//        // timeRecord Log
//        Log.i(log, "time_record_id : " + dh.getTimeRecord(time_record_id).getTime_record_id()
//                + ", activity_id : " + dh.getTimeRecord(time_record_id).getActivity_id()
//                + ", start_time : " + dh.getTimeRecord(time_record_id).getStart_time()
//                + ", finish_time : " + dh.getTimeRecord(time_record_id).getFinish_time()
//                + ", memo_in_record : " + dh.getTimeRecord(time_record_id).getMemo_in_record());
    }

    public void layoutWeightChange(float f) {
        // get childLayoutActivity
        LinearLayout childLayoutActivity = findViewById(
                parentLayoutActivity.getChildAt(0).getId());
        // layout_weight 를 f로 변경
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) childLayoutActivity.getLayoutParams();
        params.weight = f;
        childLayoutActivity.setLayoutParams(params);
    }

    public String getFormattedTime(long milliDateTime) {
        Date date = new Date(milliDateTime);
        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", systemLocale);

        return sdf.format(date);
    }

    @SuppressLint("ResourceType")
    public void setDefaultActivity(){
        setDefaultActivity("Play",
                android.R.drawable.ic_media_play, getResources().getString(R.color.blueViolet));
        setDefaultActivity("Pause",
                android.R.drawable.ic_media_pause, getResources().getString(R.color.cadetBlue));
        setDefaultActivity("Forward",
                android.R.drawable.ic_media_ff, getResources().getString(R.color.deepSkyBlue));
        setDefaultActivity("Next",
                android.R.drawable.ic_media_next, getResources().getString(R.color.orangeRed));
        setDefaultActivity("Rewind",
                android.R.drawable.ic_media_rew, getResources().getString(R.color.paleVioletRed));
        setDefaultActivity("Previous",
                android.R.drawable.ic_media_previous, getResources().getString(R.color.blueViolet));
        setDefaultActivity("Add",
                android.R.drawable.ic_menu_add, getResources().getString(R.color.cadetBlue));
        setDefaultActivity("Call",
                android.R.drawable.ic_menu_call, getResources().getString(R.color.deepSkyBlue));
        setDefaultActivity("Compass",
                android.R.drawable.ic_menu_compass, getResources().getString(R.color.orangeRed));
    }

    public void setDefaultActivity(String name, int icon, String bg_color) {
        // 기본 활동을 담을 Activity class 선언
        Activity activity = new Activity();
        // 기본 아이콘셋 id 가져와서 행동 이름과 배경 컬러까지 지정해 준 다음 DB에 add 하기
        activity.setActivity_name(name);
        activity.setActivity_icon(icon);
        activity.setActivity_bg_color(bg_color);

        dh.addActivity(activity);
        // buttonSeat 에 들어갈 기본 setActivity_id는 차피 1~9일 거라 i++ 형식으로 숫자 추가해서 insert 하면 됨.
    }

    public void setLayout() {
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View child = tableLayout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {
                    parentLayoutActivity = (LinearLayout) row.getChildAt(x); // Here you get Your View

                    if (sp.getInt("sys_run_app_first", -1) == 3) {
                        // 앱을 처음 켜는 경우 기본 버튼셋을 button_seat 테이블에 저장
                        buttonSeat = new ButtonSeat();
                        buttonSeat.setLayout_id(parentLayoutActivity.getId());
                        buttonSeat.setActivity_id(count);
                        button_seat_id = (int) dh.addButtonSeat(buttonSeat);
                    }

                    if (parentLayoutActivity instanceof LinearLayout) {
                        // 기본 활동 레이아웃 셋팅
                        // button_seat 테이블의 button_seat_id 1~9까지의 activity_id 셋팅을 불러오기
                        buttonSeat = dh.getButtonSeatById(count);
                        Activity activity = dh.getActivity(buttonSeat.getActivity_id());

                        // set parentLayoutActivity
                        parentLayoutActivity.setBackgroundColor(
                                Color.parseColor(activity.getActivity_bg_color()));
                        // get childLayoutActivity for setting
                        LinearLayout childLayoutActivity = findViewById(
                                parentLayoutActivity.getChildAt(0).getId());
                        // get/set imgActivity
                        ImageView imageView = (ImageView) childLayoutActivity.getChildAt(0);
                        imageView.setImageResource(activity.getActivity_icon());
                        // get/set txtActiveName
                        TextView txtActivityName = (TextView) childLayoutActivity.getChildAt(1);
                        txtActivityName.setText(activity.getActivity_name());
                        txtActivityName.setSelected(true); // set text flow

                        // todo : 이것도 웬만하면 DB 에서 불러와서 기록중인 버튼이 있는지 확인하는 형태로 교체
                        // todo : 나중에 if 걸어서 이 액티비티를 나갔다 와도 기록중인 상태면 알아서
                        // todo : 버튼이 기록중인 상태로 바뀌어 있도록 셋팅
                        if(buttonSeat.getTime_record_id() != null){
                            // 버튼이 기록중인 경우 레이아웃을 기록중인 상태로 변경
                            layoutWeightChange(1f);

                            // button_seat 테이블에서 이 자리(로우)에 저장된 time_record_id를 불러옴
                            time_record_id = buttonSeat.getTime_record_id();
                            // 가져온 id로 time_record 테이블에 저장된 시간 기록(timeRecord)을 불러옴
                            timeRecord = dh.getTimeRecord(time_record_id);
                            // 기록중이던 타이머 다시 돌리기
                            record = new Record(parentLayoutActivity, timeRecord);
                            record.startOrStopTimer();
                        }
                    }
                    count++; // count 로 buttonSeat 을 불러오는 것보다 더 까리한 방법이 있을 것 같은데 아닌가
                }
            }
        }
        setSharedData_sys("sys_run_app_first", 4);
//        for(Activity activity : dh.getAllActivities()){
//            Log.i(log, "activity_name : "+activity.getActivity_name());
//        }
//        for(ButtonSeat buttonSeat : dh.getAllButtonSeat()){
//            Log.i(log, "layout_id : "+buttonSeat.getLayout_id());
//        }
    }

    public void setSharedData_sys(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}