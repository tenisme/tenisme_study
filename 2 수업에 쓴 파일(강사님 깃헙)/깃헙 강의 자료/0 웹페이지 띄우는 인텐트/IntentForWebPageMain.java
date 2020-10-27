package com.block.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.ContactsContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        composeEmail(new String[]{"abc@naver.com"}, "안녕하세요");

//        createAlarm("일할시간!!", 11, 45);
//        openWebPage("http://naver.com");

    }
    // 연락처 선택
    public void selectContact(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if( i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, 1);
        }
    }

    public void composeMmsMessage(String message, Uri attachment){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("smsto:012-3455-4455"));
        i.putExtra("sms_body", message);
        i.putExtra(Intent.EXTRA_STREAM, attachment);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    public void openWebPage(String url){
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    // 원하는 시간:분  에 알람 메세지 나오도록 하는 코드
    public void createAlarm(String message, int hour, int minutes){
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    // 이메일 앱 실행시키기
    public void composeEmail(String[] addresses, String subject){
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, addresses);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }
}