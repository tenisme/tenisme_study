package cox.tenisme.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WelcomeLogIn extends AppCompatActivity {

    TextView txtWelcome2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_log_in);

        txtWelcome2 = findViewById(R.id.txtWelcome2);

        String email = getIntent().getStringExtra("email");

        txtWelcome2.setText(email+"님,\n돌아오신 것을 환영합니다!");


    }
}