package cox.tenisme.getdata;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editData;
    Button btnSendSecond;
    Button btnSendThird;

    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editData = findViewById(R.id.editData);
        btnSendSecond = findViewById(R.id.btnSendSecond);
        btnSendThird = findViewById(R.id.btnSendThird);

        btnSendSecond.setOnClickListener(this);
        btnSendThird.setOnClickListener(this);

    }

    // 버튼 누르면, 두번째 액티비티로 데이터 전달.
    // 데이터란? 에디트 텍스트에서 문자열 가져온다.
    @Override
    public void onClick(View v) {
        if(v == btnSendSecond){
            // 보낼 데이터를 String 타입으로 변환
            String data = editData.getText().toString().trim();
            // 액티비티 전환을 위해 Intent 클래스 소환
            i = new Intent(MainActivity.this, SecondActivity.class);
            i.putExtra("data", data);
            i.putExtra("hiddenData", 10); // 유저 입력에 상관없이 그냥 보내는 데이터
            // 시이작
            // 시작하면서 데이터를 받아오기 위해서는 startActivityForResult(); 함수를 이용해야 한다
            startActivityForResult(i, 0); // requestCode 로 숫자를 하나 보내준다 >> 0 // 보내면서 받아올 준비가 된 것임.
            // 리퀘스트 코드가 필요한 이유 : 한 액티비티에서 여러 액티비티에 연결될 수 있기 때문에 그 때마다 리퀘스트 코드를 달리해줘야 구분이 된다.
                // ★requestCode 숫자 겹치면 안됨 주의★
        }else if(v == btnSendThird){
            // 세번째 액티비티로 전환되는 버튼
            String data = editData.getText().toString().trim();
            i = new Intent(MainActivity.this, ThirdActivity.class);
            i.putExtra("data", data);
            startActivityForResult(i, 1);
        }
    }

    // 다른 액티비티가" "되돌려" 보내준 데이터를 받을 수 있는 함수 onActivityResult()
    // 모든 안드로이드 액티비티들은 이 코드로 데이터들을 주고받는다. 중요하다는 말임.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 첫 액티비티의 requestCode 와 두번째 액티비티의 resultCode 가 일치하면 두번째 액티비티에서 데이터를 가져올 수 있다.
        if(requestCode == 0 && resultCode == RESULT_OK){
            //두번째 액티비티가 보낸 정보를 'data' 변수로 가져온다
            String message = data.getStringExtra("secondData");
            editData.setText(message);
        }
        // 3번째 액티비티한테 "되돌려" 받은 데이터 처리
        // 그냥 별도로 다시 if 부터 써줘도 됨.
        else if(requestCode == 1 && resultCode == RESULT_OK){
            String message2 = data.getStringExtra("thirdData");
            editData.setText(message2);
        }
    }
}