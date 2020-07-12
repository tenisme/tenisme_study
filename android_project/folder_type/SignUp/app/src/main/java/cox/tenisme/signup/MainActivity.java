package cox.tenisme.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail;
    EditText editPw;
    EditText editPwCheck;
    Button btnSign;
    Button btnLogIn;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.editEmail);
        editPw = findViewById(R.id.editPw);
        editPwCheck = findViewById(R.id.editPwCheck);
        btnSign = findViewById(R.id.btnSign);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnSign.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnSign){
            String editEmailS = editEmail.getText().toString();
            String editPwS = editPw.getText().toString();
            String editPwCheckS = editPwCheck.getText().toString();


            if(editEmailS.indexOf("@") == -1 || editEmailS.indexOf(".") == -1 || editEmailS.isEmpty()){
                Toast.makeText(MainActivity.this, "이메일을 바르게 입력하세요", Toast.LENGTH_SHORT).show();
                return; // 리턴 안하면 이거 실행하고 아래것도 다 진행함 리턴 필수임
            }

            if(editPwS.length() > 12 || editPwS.length() < 6 || editPwS.isEmpty()){
                Toast.makeText(MainActivity.this, "비밀번호는 6자리 이상,\n12자리 이하로 적어주십시오", Toast.LENGTH_SHORT).show();
                return;
            }

            if(editPwCheckS.compareTo(editPwS) != 0 || editPwCheckS.isEmpty()){
                Toast.makeText(MainActivity.this, "비밀번호를 일치시켜 주십시오", Toast.LENGTH_SHORT).show();
                return;
            }

            i = new Intent(MainActivity.this, ChooseAvatar.class);
            i.putExtra("email",  editEmailS);
            i.putExtra("passwd", editPwS);
            startActivity(i);

            finish();
        }
        if(v == btnLogIn){
            i = new Intent(MainActivity.this, LogIn.class);
            startActivity(i);
        }
    }
}