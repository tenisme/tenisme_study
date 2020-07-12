package cox.tenisme.mytest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText editName;
    EditText editPhone;
    ImageView btn_star_big_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = findViewById(R.id.txtTitle);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btn_star_big_off = findViewById(R.id.btn_star_big_off);

        Button btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyTest", "Click Me!");

                // 로그에 출력하기 위해서 txtTitle 을 String 타입으로 변환함
                String text = txtTitle.getText().toString();
                Log.i("MyTest", "get text : "+text);

                // txtTitle 의 내용을 editName, editPhone 에 입력한 내용으로 변환하기 위해
                // editName 과 editPhone 모두를 String 타입으로 변환한다
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();

                // xml 에서 불러온 text 내용 변경하기 setText();
                txtTitle.setText(name + "\n" + phone);

                // 버튼을 누르면 toast 화면 띄우기
                Toast.makeText(MainActivity.this, name + "\n" + phone,
                        Toast.LENGTH_LONG).show();

                // 버튼을 누르면 btn_star_big_off 이미지를 ~_on 으로 변경하기
                btn_star_big_off.setImageResource(android.R.drawable.btn_star_big_on);

            }
        });

    }
}