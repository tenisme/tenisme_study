package cox.tenisme.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail;
    EditText editPw;
    Button btnLogIn;
    CheckBox checkAutoLogIn;

    String savedEmail;
    String savedPw;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editEmail = findViewById(R.id.editEmail);
        editPw = findViewById(R.id.editPw);
        btnLogIn = findViewById(R.id.btnLogIn);
        checkAutoLogIn = findViewById(R.id.checkAutoLogIn);

        sp = getSharedPreferences("SignUp", MODE_PRIVATE);
        savedEmail = sp.getString("email", null);
        savedPw = sp.getString("passwd", null);

        btnLogIn.setOnClickListener(this);

        // 자동 로그인이 쉐어드 프리퍼런스에 저장되어있는지 정보를 가져온다.
        boolean autoLogin = sp.getBoolean("auto_login", false);
        // 자동 로그인이 true 로 되어있으면 이메일과 패스워드를 에디터 텍스트에 자동으로 입력한다.
        // 체크박스에도 체크 표시를 해놓는다.
        if(autoLogin == true){
            editEmail.setText(savedEmail);
            editPw.setText(savedPw);
            checkAutoLogIn.setChecked(true);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == btnLogIn) {
            String editEmails = editEmail.getText().toString().trim();
            String editPws = editPw.getText().toString().trim();

            if (savedEmail != null && savedPw != null && savedEmail.equals(editEmails)
                    && savedPw.equals(editPws)) {
                // 자동 로그인 채크박스에 표시가 되었는지 안 되었는지를 여기서(★로그인 버튼을 클릭하면★) 확인한다.

                if(checkAutoLogIn.isChecked()){
                    editor = sp.edit();
                    editor.putBoolean("auto_login", true);
                    editor.apply();
                }else{
                    editor = sp.edit();
                    editor.putBoolean("auto_login", false);
                    editor.apply();
                }
                i = new Intent(LogIn.this, WelcomeLogIn.class);
                i.putExtra("email", editEmails);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(LogIn.this, "등록되지 않은 이메일이나 비밀번호입니다",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}