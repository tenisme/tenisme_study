package cox.tenisme.fab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editItem;
    EditText editQuantity;
    EditText editColor;
    EditText editSize;
    Button btnSave;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 설정 코드
        Toolbar toolbar = findViewById(R.id.toolbar);
        // 툴바 돌아가게 만드는 코드
        setSupportActionBar(toolbar);

        // fab 버튼 설정 코드
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();

//                // 스낵바 코딩
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                    // null > 눌렀을 때 뭔가 할 수 있다는 말임. 그거 처리하는 것이 지금은 null 로 되어있음.
//                    // 화면에 보여주기만 할 거면 Toast 만 쓰면 됨.
            }
        });
    }

    // 알러트 다이얼로그 메소드
    public void createPopupDialog(){

        // xml 파일을 가져와서 AlertDialog 띄우는 방법
        // 1. AlertDialog 객체 생성
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        // 2. 만들어놓은 xml 파일을 View 타입 객체로 가져온다 (뷰를 가져온다)
        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog,null);

        // 3. 멤버변수 뷰들을 여기서 연결하기
            // 이 액티비티에 대응하는 xml 과 연결해서 뷰들을 가져올 게 아니면,
            // findViewById() 앞에 꼭 View 객체의 참조변수 이름을 붙여주자.
        editItem = alertView.findViewById(R.id.editItem);
        editQuantity = alertView.findViewById(R.id.editQuantity);
        editColor = alertView.findViewById(R.id.editColor);
        editSize = alertView.findViewById(R.id.editSize);
        btnSave = alertView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4. 활용을 위해, 클릭하면 edit~에 적힌 것들을 String 으로 바꿔 저장
                String item = editItem.getText().toString().trim();
                // String 이외의 자료형은 일단 문자로 가져오고(.getText().toString()) 난 다음 숫자로 가져온다
                    // 아무 것도 입력되지 않았는데 숫자로 바꾸라고 하면(Integer.parseInt()) 에러나면서 꺼진다
                    // 그러므로 일단 int 로 가져와야 하는 것도 String 으로 가져온 다음에,
                    // 아래 에러 판별 if 문에서 에러 처리가 되지 않으면 그 다음에 int 로 형변환을 한다.
                String quantity = editQuantity.getText().toString().trim();
                String color = editColor.getText().toString().trim();
                String size = editSize.getText().toString().trim();

                // 4-1. quantity 와 size 를 입력하지 않으면 에러 처리
                    // 에러 판별의 적절한 위치가 포인트.
                if(quantity.isEmpty() || size.isEmpty()) {
                    Toast.makeText(MainActivity.this, "숫자를 입력하세요", Toast.LENGTH_SHORT).show();
                    return; // 에러에는 리턴필수.
                }

                // 4-2. 그 다음에 숫자여야 하는 String 들을 int 로 변환한다.
                int quantity_int = Integer.parseInt(quantity);
                int size_int = Integer.parseInt(size);

                Log.i("AAA", item+", "+quantity_int+", "+color+", "+size_int);

                // 5. AlertDialog 를 종료
                dialog.cancel();
            }
        });

        // 6. 다이얼로그 객체에 뷰 객체를 셋팅한다
        alert.setView(alertView);
        // 6-1.다이얼로그 외의 다른 곳을 클릭하지 못하게 하는 다이얼로그 함수 작성
        alert.setCancelable(false);

        // 7. AlertDialog "생성"(일단 멤버변수로 생성 후 여기서 초기화)
            // alert 변수를 create(); 하는 dialog 를 설정
            // 커스텀 다이얼로그는 "이렇게" "시작"과 끝을 써줘야 만들어진다.
        dialog = alert.create();
        // 7-1. 다이얼로그를 "표시"하는 함수 작성
        dialog.show();
        // 처리 끝
    }

    // R.menu.menu_main.xml 파일을 엮어준다.
    // 이외의 다른 xml 파일도 엮을 수 있다.
    // 이 함수는 손댈 필요가 없다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 우측 상단 메뉴 버튼들을 누르면 무슨 액션을 할지 설정하는 곳
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // 버튼 아이템 빼내는 코드
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        // 바로 아래는 action_settings 아이디를 가진 버튼의 액션 설정
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_add) {
            Toast.makeText(MainActivity.this, "추가 버튼 누름", Toast.LENGTH_SHORT).show();
            return true; // 버튼의 마지막에는 이걸 꼭 해줘야 함. 그래야 이 아래를 실행을 안 함.
        } else if (id == R.id.menu_delete) {
            Toast.makeText(MainActivity.this, "삭제 버튼 누름", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}