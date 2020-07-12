package cox.tenisme.section2apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Year;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editYear;
    TextView txtCalYear;
    Button btnCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editYear = findViewById(R.id.editYear);
        txtCalYear = findViewById(R.id.txtCalYear);
        btnCal = findViewById(R.id.btnCal);

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1. 현재 날짜를 가져오고 int 타입으로 저장
                Calendar today = Calendar.getInstance();
                int yearNow = today.get(Calendar.YEAR);

                // 2. editYear 에 입력한 숫자를 int 타입으로 저장
                Integer editYearInt = Integer.parseInt(""+editYear.getText());

                // 3. 현재날짜 - editYear 을 계산하는 변수 저장
                int calAge = yearNow - editYearInt;

                // 4. 현재날짜 - editYear 를 txtCalYear 에 띄우기
                txtCalYear.setText("Your cat's age : "+calAge+" years old");

                // 5. editYear 에 적은 내용 지우기
                editYear.setText(""); // setText(null)도 가능함.
            }
        });

//                // 강사님 ver.
//                // 1. editYear 에 적혀있는 글자 가져오기
//                String catYear = editYear.getText().toString();
//                Log.i("MyCat", "유저가 입력한 값은 : "+catYear);
//
//                // 2. 이번 년도에서, 가지고 온 년도를 뺀다
//                int catAge = 2020 - Integer.parseInt(catYear);
//                Log.i("MyCat", "계산한 나이는 : "+catAge);
//
//                // 3. calAge 에 표시하기
//                txtAge.setText(""+catAge);
//
//                // 4. editYear 내용 지우기
//                editYear.setText(""); // setText(null); 도 가능함.
//            }
//        });

    }
}