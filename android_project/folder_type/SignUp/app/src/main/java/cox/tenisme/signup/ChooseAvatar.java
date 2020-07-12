package cox.tenisme.signup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseAvatar extends AppCompatActivity implements View.OnClickListener{

    ImageView viewImage;
    Button btnRabbit;
    Button btnTurtle;
    Button btnChoose;

    String email;
    String passwd;
    int chooseImg = 0;

    Intent i;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_avatar);

        viewImage = findViewById(R.id.viewImage);
        btnRabbit = findViewById(R.id.btnRabbit);
        btnTurtle = findViewById(R.id.btnTurtle);
        btnChoose = findViewById(R.id.btnChoose);

        email = getIntent().getStringExtra("email");
        passwd = getIntent().getStringExtra("passwd");

        btnRabbit.setOnClickListener(this);
        btnTurtle.setOnClickListener(this);
        btnChoose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnRabbit){

            viewImage.setImageResource(R.drawable.rabbit);
            chooseImg = 1;

        }else if(v == btnTurtle){

            viewImage.setImageResource(R.drawable.turtle);
            chooseImg = 2;

        }else if(v == btnChoose){

            if(chooseImg == 0){
                Toast.makeText(ChooseAvatar.this, "아바타를 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder completeAlert = new AlertDialog.Builder(ChooseAvatar.this);
            completeAlert.setTitle("회원가입 완료");
            completeAlert.setMessage("가입을 완료하시겠습니까?");
            completeAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    i = new Intent(ChooseAvatar.this, ClearSignUp.class);
                    i.putExtra("email", email);
                    i.putExtra("passwd", passwd);
                    if(chooseImg == 1){
                        i.putExtra("avatar", R.drawable.rabbit);
                    }else if(chooseImg ==  2){
                        i.putExtra("avatar", R.drawable.turtle);
                    }

                    startActivity(i);
                    finish();
                }
            });
            completeAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            completeAlert.setCancelable(false);
            completeAlert.show();

        }
    }

}