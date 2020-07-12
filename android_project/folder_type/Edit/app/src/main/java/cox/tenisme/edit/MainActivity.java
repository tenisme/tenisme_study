package cox.tenisme.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        // 텍스트 와쳐
        // addTextChangedListener() > editText 의 텍스트가 바뀔 때마다 감지하겠다
        // textWatcher() > 지켜보겠다
        // 글자가 하나하나 바뀔 때마다 아래의 세가지 함수가 계속 돌아간다.
        // CharSequence s > 문자열은 아니지만 문자열로 바꿀 수 있다 > s.toString()
            // 사용자가 입력한 문자열을 가져오는 파라미터이다.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Toast.makeText(MainActivity.this, "텍스트 바뀌기 전 : "+s.toString(), Toast.LENGTH_SHORT).show();
            }

            // 요 함수가 핵심
            // 글자가 하나 생길 때마다 실행된다
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(MainActivity.this, "텍스트가 바뀌고 있음 : "+s.toString(), Toast.LENGTH_SHORT).show();
                // 글자가 하나씩 바뀔 때마다 textView 에 입력한다.
                textView.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(MainActivity.this, "텍스트가 바뀐 후 : "+s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}