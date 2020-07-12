package cox.tenisme.getdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtData;
    EditText editSend;
    Button btnSecond;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtData = findViewById(R.id.txtData);
        editSend = findViewById(R.id.editSend);
        btnSecond = findViewById(R.id.btnSecond);

        // 일단 Intent 에서 데이터를 가져올 거라고 선언한다
        i = getIntent();
        // 키값들을 가져옴
        String data = i.getStringExtra("data");
        int hiddenData = i.getIntExtra("hiddenData", 0);
        // 받은 데이터를 txtData 에 표시
        txtData.setText(data);

        btnSecond.setOnClickListener(this);
    }
    // 버튼을 눌렀을 때,


    // finish()를 해준다. : 이 액티비티를 종료해 첫 번째 액티비티로 돌아가기 위해서
    @Override
    public void onClick(View v) {
        if(v == btnSecond){
            // 데이터를 보내기  위해 에디트 텍스트에서 문자를 가져온다.
            String data = editSend.getText().toString().trim();
            // 앞 액티비티로 데이터를 전달하고
            i = getIntent(); // 전에 가져왔던 액티비티한테 다시 줘야하기 때문에 Intent 를 새로 만드는 게 아니라, 이전 액티비티에서 가져온다
            i.putExtra("secondData", data); // 일단 보냄
            setResult(RESULT_OK, i); // resultCode 보내기 + 데이터를 보내줬던 액티비티로 화면을 다시 돌려보낸다(다시 돌아가라)
            finish(); // 액티비티 종료
        }
    }
}