package com.tenisme.alarmmanagerexam;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// 다이얼로그 프래그먼트 작성
// 타임 피커만 사용하지 않고 다이얼로그 프래그먼트를 함께 사용한 이유 :
// 화면을 돌려도 나타난 타임 피커가 사라지지 않도록 할 수 있기 때문이다.
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private AlarmManager alarmManager;

    // onCreateDialog : 다이얼로그를 리턴하도록 되어있는 함수.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // 알람매니저 가져오기
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        // 현재 시간 셋팅
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 파라미터 설명
        // 두번째 파라미터 : 리스너를 만드는 곳
        // 세번째, 네번째 파라미터 : (현재) 시간, 분.
        // 다섯번째 파라미터 : 24시간 형태로 보여줄 것인지, 12시간 형태로 보여줄 것인지를 설정.
        //                    true/false, 혹은 DateFormat.is24HourFormat(getContext())로 설정.
        //                    DateFormat.is24HourFormat(getContext()) : 현재 컨텍스트(내 기기)의 설정을 가져온다.
        return new TimePickerDialog(getContext(), this, hour, minute, DateFormat.is24HourFormat(getContext()));
    }

    // 리스너(this) 구현
    // 타임 피커를 띄워놓고 유저가 시간을 선택했을 때 이게 호출됨. 유저가 선택한 시간과 분이 여기로 들어오게 됨.
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        // 시간 셋팅 (유저가 입력한 것)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(getContext(), MainActivity.class);
        // flags 자리(맨 마지막 파라미터 자리)에는 뭔지 잘 모르겠으면 그냥 0을 주면 된다.
        PendingIntent operation = PendingIntent.getActivity(getContext(), 0, intent, 0);

        // 유저가 설정한 시간에 알람이 울리도록 설정
        // 첫번째 파라미터 : 알람 타입 설정
        // 두번째 파라미터 : 언제 알람이 발동되어야 하는지 트리거가 필요함.
        // 세번째 파라미터 : 뭐가 실행될 것인지는 PendingIntent 로 설정한다.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation);
    }
}
