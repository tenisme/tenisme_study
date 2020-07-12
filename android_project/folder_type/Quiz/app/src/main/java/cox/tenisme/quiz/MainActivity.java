package cox.tenisme.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.text.TextUtils.concat;

public class MainActivity extends AppCompatActivity {

    TextView txtQuestion;
    ProgressBar quizPB;
    TextView txtStats;
    Button btnTrue;
    Button btnFalse;

    // 선행1 : activity_main.xml 에서 java(액션)에 필요한 구성요소들 가져오기
        // onCreate()에서 변수 초기화, 버튼별 setOnClickListener()도 생성
    // 선행2 : 문제와 답을 다루기 위한 클래스 제작
        // 문제, 답 변수는 private
        // 생성자로 문제와 답을 입력받아 저장하기
    // 선행3 : Android > app > res > values > strings.xml 에서 문제 입력해서 저장하기

    // 1. 문제를 배열 타입의 멤버 변수로 한번에 저장하기
    QuizModel[] quizModels = new QuizModel[] {
            new QuizModel(R.string.q1, true),
            new QuizModel(R.string.q2, false),
            new QuizModel(R.string.q3, true),
            new QuizModel(R.string.q4, false),
            new QuizModel(R.string.q5, true),
            new QuizModel(R.string.q6, false),
            new QuizModel(R.string.q7, true),
            new QuizModel(R.string.q8, false),
            new QuizModel(R.string.q9, true),
            new QuizModel(R.string.q10, false)
    };
    // 2. quizModels 의 인덱스를 불러올 숫자 변수 생성
    int quizModelsIndex; // 이렇게 int 타입을 선언만 하면 기본 셋팅값은 0으로 저장된다.
    // 4. 문제가 맞을 때마다 1씩 증가하는 int 타입의 변수를 생성
        // 이 변수는 txtStats 에 맞은 문제 수의 통계를 표시하려는 용도임.
    int countCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtQuestion = findViewById(R.id.txtQuestion);
        quizPB = findViewById(R.id.quizPB);
        txtStats = findViewById(R.id.txtStats);
        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);

        // Click btnTrue
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 7. judgeAnswer 를 활용해 정답을 판별하기 (true/false 공통)
                    // True 를 클릭했으므로 userAnswer 는 true 를 입력해준다.
                judgeAnswer(true);

                // 8. 버튼을 클릭할 때마다 다음 문제로 넘어가도록 만들기 (true/false 공통)
                // 8-1. quizModeIndex 를 +1 (다음 문제를 불러내기 위해 index 숫자를 증가)
                quizModelsIndex = (quizModelsIndex + 1) % 10;
                    // 나누기 10의 나머지를 구하게 해 카운트가 9를 넘어가도 다시 0부터 시작하게 만든다.

                // 9. 를 실행
                alert(quizModelsIndex);

                // 8-2. ( ~ + 1) % 10 이 된 숫자로 quizModels 에 저장된 "문제"를 불러내 txtQuestion 에 표시하기
                txtQuestion.setText(quizModels[quizModelsIndex].getQuiz());
                    // 8-2가 alert 밑으로 내려온 이유 : 0이 되면 일단 나갈 거냐고 물어본 다음에
                    // 안 나가면 그 다음에 다음 문제를 보여줘도 되기 때문임.
            }
        });

        // Click btnFalse
        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                judgeAnswer(false);

                quizModelsIndex = (quizModelsIndex + 1) % 10;

                alert(quizModelsIndex);

                txtQuestion.setText(quizModels[quizModelsIndex].getQuiz());
            }
        });

        // 3. 앱 실행시 txtQuestion 에 첫 번째 문제 띄워놓기
        txtQuestion.setText(quizModels[quizModelsIndex].getQuiz());
            // quizModels 의 quizModelsIndex 번호(기본 셋팅 0)의 quiz 를 가져옴(get).
        // 5. 앱 실행시 txtStats 에서 countCorrect 개수 표시 (기본 플랫폼 셋팅)
        txtStats.setText("Corrects count : "+countCorrect);
    }

    // 6. 답을 받아서, 답이 맞을 경우 countCorrect 를 +1하고 Toast 로 "Correct Answer"를 띄우고,
    // 답이 아닐 경우 "Incorrect Answer"를 띄우는 멤버 메소드 만들기
        // 돌려받을 값이 없으므로 void
        // 답을 판별한 후 다시 txtStats 에 마지막 개수 표시하기
    void judgeAnswer(boolean userAnswer) {
        if(userAnswer == quizModels[quizModelsIndex].getAnswer()){
            countCorrect = countCorrect + 1;
            Toast.makeText(MainActivity.this, "Correct Answer", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Incorrect Answer", Toast.LENGTH_SHORT).show();
        }
        txtStats.setText("Corrects count : "+countCorrect);

        // 10. 버튼을 누를 때마다 프로그레스 바 수치를 1씩 늘리기
            // 만드는 걸 깜박해서 10번째 순서 :3
        quizPB.incrementProgressBy(1);
    }

    // 9. 배열 인덱스가 0으로 돌아올 때마다 AlertDialog 를 띄우는 멤버 메소드 만들기
    void alert(int index){
        if(index == 0){
            AlertDialog.Builder quizAlert = new AlertDialog.Builder(MainActivity.this);
            quizAlert.setTitle("Quit Quiz App");
            // 문제를 맞춘 개수에 따라 다른 alert 메세지를 출력하기
            if(countCorrect == 10){
                quizAlert.setMessage("You've get Star!\nYour total score : "+countCorrect);
            }else if(countCorrect == 9){
                quizAlert.setMessage("Too bad..\nYour total score : "+countCorrect);
            }else if(countCorrect > 6){
                quizAlert.setMessage("Well done.\nYour total score : "+countCorrect);
            }else if(countCorrect > 3){
                quizAlert.setMessage("Just a little more try.\nYour total score : "+countCorrect);
            }else if(countCorrect >= 0){
                quizAlert.setMessage("Oops.\nYour total score : "+countCorrect);
            }
            quizAlert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            quizAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 문제를 전부 맞추면 컨티뉴 이후 버튼 양 옆에 ★을 붙여주는 기능
                    String btnTrueS = btnTrue.getText().toString();
                    String btnFalseS = btnFalse.getText().toString();
                    if(countCorrect == 10){
                            String changeBtnTrueS = (String) concat("★", btnTrueS, "★");
                            btnTrue.setText(changeBtnTrueS);
                            String changeBtnFalseS = (String) concat("★", btnFalseS, "★");
                            btnFalse.setText(changeBtnFalseS);
                    }
                    // Continue 하면 return;하기 전에 특정 변수 초기화시키기
                    countCorrect = 0;
                    txtStats.setText("Corrects count : "+countCorrect);
                    quizPB.setProgress(0);
                    return;
                }
            });
            quizAlert.show();
            return;
        }
    }

}