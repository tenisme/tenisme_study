package cox.tenisme.startendalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgAlarm;
    TextView txtSec;
    EditText editSec;
    Button btnCancel;
    Button btnStart;

    // 카운트다운 타이머 셋팅
    CountDownTimer timer;
    int sec; // 초(유저용, "보여주는" 용도)
    long milliSec; // 함수용(타이머용)

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAlarm = findViewById(R.id.imgAlarm);
        txtSec = findViewById(R.id.txtSec);
        editSec = findViewById(R.id.editSec);
        btnCancel = findViewById(R.id.btnCancel);
        btnStart = findViewById(R.id.btnStart);

        mp = MediaPlayer.create(this, R.raw.alarm);

        btnStart.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btnStart){
            // "타이머 시작" 버튼을 누르면 실행되는 행동

            //// 유저가 입력한 초를 타이머 제한시간으로 설정하는 과정 ////
            // "유저가 입력한 초(editSec)를 활용하기 위해" 일단 문자를 String 으로 바꿔 가져오고 int 타입으로 변환
            String secStr = editSec.getText().toString().trim();
            sec = Integer.parseInt(secStr);
            // "유저가 입력한 숫자로 카운트다운 타이머 셋팅을 하기위해" sec 에 *1000을 하고 long 타입으로 변환
            milliSec = (long) sec * 1000;
            // milliSec(editSeconds)를 타이머 제한시간으로 설정, 간격은 1초(1000)로 설정
            timer = new CountDownTimer(milliSec,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // 시간이 흐를 때마다 실행되는 행동
                    int remainingTime = (int) millisUntilFinished / 1000;
                    txtSec.setText(""+remainingTime);
                    if(remainingTime == 0){
                        timer.onFinish();
                        return;
                    }
                }
                @Override
                public void onFinish() {
                    // 시간이 다 흐르고 난 다음 실행되는 행동
                    txtSec.setText("Timer's End!");
                    mp.start();
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(5)
                            .playOn(imgAlarm);
                }
            };
            // ★★★★★★★★★★
            timer.start();
            // ★★★★★★★★★★
        }else if(v==btnCancel){
            // "타이머 취소" 버튼을 누르면 실행되는 행동
            timer.cancel();
            txtSec.setText(""+sec);
        }
    }
}