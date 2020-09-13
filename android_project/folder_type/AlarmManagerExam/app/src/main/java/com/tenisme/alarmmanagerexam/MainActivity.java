package com.tenisme.alarmmanagerexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

// TODO : 안됨..ㅠ 나중에 쓸 거면 로그 찍어보면서 디버깅하든지 하기.

// 안드로이드 내부에서 자체적으로 알람 서비스를 제공함.
// 내부적으로 지정된 시간에 무엇을 하게 할 수 있는 스케줄링 기능을 수행함.
// 통상적인 기상 알람에 사용되는 게 아님.
// 백그라운드에서 어떤 시간을 지정해놓고 그 시간에 무언가를 해야할 때 알람매니저를 수행.
// 알람 매니저(플랫폼이 제공하는 서비스) 는 백그라운드에서 돌아가기 때문에 앱을 종료하고 나서도 타이머가 돌아간다.
// 정확한 시간이 아닐 수 있다.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showAlarmDialog(View view) {
        // 지정된 시간에 무엇이 동작하게끔 설정하는 가장 간단한 방법은 타임 피커를 "활용"하는 것.
        TimePickerFragment timePickerFragment = new TimePickerFragment();

        // 타임 피커 프레그먼트를 보여줌.
        // getSupportFragmentManager() : 프래그먼트 매니저를 가져옴.
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }
}