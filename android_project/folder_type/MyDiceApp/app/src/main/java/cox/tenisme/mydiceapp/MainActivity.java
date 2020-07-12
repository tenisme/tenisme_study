package cox.tenisme.mydiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btnRoll;
    ImageView diceImg1;
    ImageView diceImg2;
    MediaPlayer mp;

    int[] diceImages = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4,
            R.drawable.dice5, R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRoll = findViewById(R.id.btnRoll);
        diceImg1 = findViewById(R.id.diceImg1);
        diceImg2 = findViewById(R.id.diceImg2);
        mp = MediaPlayer.create(this, R.raw.dice_sound);

        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyDiceApp","주사위 버튼 눌렀음!");

                Random rand = new Random();
                int diceNumber = rand.nextInt(6);
                Log.i("MyDiceApp", ""+diceNumber);

                diceImg1.setImageResource(diceImages[diceNumber]);

                // 랜덤 숫자 따로 한 번 더 돌리고 그 숫자는 diceImg2에 활용하기
                diceNumber = rand.nextInt(6);
                Log.i("MyDiceApp", ""+diceNumber);

                diceImg2.setImageResource(diceImages[diceNumber]);

                mp.start();

                YoYo.with(Techniques.RollIn)
                        .duration(300) // 초(밀리세컨. 2000 = 2초)
                        .repeat(1)
                        .playOn(diceImg1);
                YoYo.with(Techniques.RollIn)
                        .duration(300)
                        .repeat(1)
                        .playOn(diceImg2);

            }
        });
    }
}


