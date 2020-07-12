package cox.tenisme.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ClearSignUp extends AppCompatActivity {

    ImageView imgAvatar;
    TextView txtEmail;

    String email;
    String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_sign_up);

        imgAvatar = findViewById(R.id.imgAvatar);
        txtEmail = findViewById(R.id.txtEmail);

        email = getIntent().getStringExtra("email");
        passwd = getIntent().getStringExtra("passwd");
        int avatar = getIntent().getIntExtra("avatar", 0);

        txtEmail.setText(email);
        imgAvatar.setImageResource(avatar);

        SharedPreferences sp = getSharedPreferences("SignUp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("passwd", passwd);
        editor.apply();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}