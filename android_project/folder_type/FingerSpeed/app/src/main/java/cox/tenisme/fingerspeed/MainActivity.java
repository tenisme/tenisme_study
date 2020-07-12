package cox.tenisme.fingerspeed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 탭 카운트의 숫자 고정시키기(상수 만들기).
    public static final int TAB_COUNT = 100;

    TextView txtTimer;
    TextView txtCount;
    Button btnTap;
    Button btnCheat;

    // 타이머 멤버변수 선언
    CountDownTimer countDownTimer;
    // 카운트다운 타이머의 초들을 멤버 변수로 빼서 관리하기 쉽도록 만든다
    long initCountMillis = 20000; // 20초 제한 카운트
    int timerInterval = 1000; // 1초씩 카운트
    int tabCount = TAB_COUNT; // tabCount 의 기본값을 TAB_COUNT 의 값인 10으로 설정한다. tabCount 자체가 상수가 되는 것은 아니다.
    int remainingTime; // 남은 시간을 1/1000으로 표시

    String alertMessage = "다시 도전하시겠습니까?";
    String alertSuccess(int remainingTime){
        return "기록은 "+((initCountMillis/1000)-remainingTime)+"초입니다! ";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer = findViewById(R.id.txtTimer);
        txtCount = findViewById(R.id.txtCount);
        btnTap = findViewById(R.id.btnTap);
        btnCheat = findViewById(R.id.btnCheat);

        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickBtn(1);

                }
        });

        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 치트 버튼 클릭시 행동

                clickBtn(10);

            }
        });

        // 타이머 변수 초기화 : 60초 한도로, 1초씩 감소하도록
        countDownTimer = new CountDownTimer(initCountMillis, timerInterval) {
            @Override
            public void onTick ( long millisUntilFinished){
                // 시간이 1000씩 감소할 때마다 실행되는 함수

                // 1000이 1(초)로 표시되도록 1000을 나눈 수를 int 타입으로 저장한다
                // 온틱 함수가 끝나도 시간이 초기화되지 않도록 아래 변수를 멤버변수로 바꿔준다
                remainingTime = (int) millisUntilFinished / 1000;
                Log.i("Finger","남은 시간 : "+remainingTime);
                // txtTimer 에 타이머가 표시되도록 한다.
                txtTimer.setText(""+remainingTime);
            }

            @Override
            public void onFinish () {
                // 타이머가 종료되면(0이 되면) 안내 토스트가 뜨도록 한다
                Toast.makeText(MainActivity.this, "시간이 종료되었습니다.", Toast.LENGTH_SHORT).show();

                if(tabCount > 0){
                    showMyAlert("실패", alertMessage);
                }else{
                    showMyAlert("성공", alertSuccess(remainingTime)+alertMessage);
                }
            }
        };
        // onCreate 에서 카운트다운 타이머 "시작"
            // 타이머 만들어놓고 이거 안 쓰면 타이머 안 돌아감.
        countDownTimer.start();

        // txtCount 에 tabCount 의 숫자를 띄워놓는다
        txtCount.setText(""+tabCount);

    }

    // 경우에 따라 다른 알람을 띄우는 함수
    void showMyAlert(String title, String message){
        AlertDialog.Builder finishAlert = new AlertDialog.Builder(MainActivity.this);
        finishAlert.setTitle(title);
        finishAlert.setMessage(message);
        finishAlert.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tabCount = TAB_COUNT;
                txtTimer.setText(""+countDownTimer.start());
                txtCount.setText(""+tabCount);
                return;
            }
        });
        finishAlert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        if(finishAlert.show().isShowing()){
            return;
        }
        finishAlert.setCancelable(false);
        finishAlert.show();
    }

    //  버튼 클릭시 기본 실행 함수
    void clickBtn(int clickCount){
        // 타이머 남은 시간이 0인지 체크해서 0이면 아래를 실행시키지 않는다.
        // "버튼을 누를 때" ~면 감소시키지 않는다 이므로 온 클릭 함수 안에서 만들어줘야 한다.
        if(remainingTime == 0){
            return;
        }

        // 버튼을 누를 때마다 숫자를 1씩 감소시키고 감소된 숫자를 txtCount 에 표시한다.
        tabCount = tabCount - clickCount;
        txtCount.setText(""+tabCount);
        // 티쳐 방법 (나랑 코드 설계가 다름. 참고만 하기)
//        if(tabCount > 10){
//            tabCount = tabCount - 10;
//            txtCount.setText(""+tabCount);
//        }

         // 탭 수가 0이 되면 더이상 숫자가 내려가지 않도록 하고,  성공 Alert 띄우기
        if(tabCount <= 0){
            txtCount.setText("0");
            countDownTimer.cancel();
            Toast.makeText(MainActivity.this, "성공!", Toast.LENGTH_SHORT).show();
            showMyAlert("성공", alertSuccess(remainingTime)+alertMessage);
            return;

        }
    }
}