package cox.tenisme.intent;

import androidx.annotation.Nullable;
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

//        selectContact();

//        composeMMsMessage("안녕하세요", null);

//        openWebPage("https://m.naver.com");

//        createAlarm("공부햄",11, 41);

//        composeEmail(new String[]{"abc@naver.com"},"안녕하세요");
    }

    // Intent 로 연락처 주소록 띄우기
    public void selectContact(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i,1);
        }
    }

    // Intent 로 MMS 메세지 화면 띄우기
    // 선행 : 안드로이드 매니페스트 설정 필요함
    public void composeMMsMessage(String message, Uri attachment){
        Intent i = new Intent(Intent.ACTION_SEND); // 보낼 거니까 send
        i.setData(Uri.parse("smsto:01333216401"));
        i.putExtra("sms_body", message);
        i.putExtra(Intent.EXTRA_STREAM, attachment);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    // Intent 로 웹브라우저 실행시키는 함수
    public void openWebPage(String url){
        Uri webPage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webPage);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    // 원하는 시간/분에 알람 메세지가 나오도록 하는 함수
    // 선행 : 매니페스토에 알람 설정 권한을 줘야 함
    // 인텐트를 받아줄 수 있도록 처리하는 앱이어야 실행 가능 앱 목록에 뜬다.
    public void createAlarm(String message,int hour,int minute){
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minute);
        // 시간 단위는 0시부터 23시까지이다.
        // 위 인텐트가 실행될 수 있는 앱이 있는지 확인하는 if(){}
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    // 메일 앱 실행시킬 수 있는 함수
    public void composeEmail(String[] addresses, String subject){
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, addresses);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }


    // 여기서 연락처 정보를 가져올 수 있음.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}