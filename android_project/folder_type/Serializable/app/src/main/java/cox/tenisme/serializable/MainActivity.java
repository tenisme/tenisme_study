package cox.tenisme.serializable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cox.tenisme.serializable.model.Person;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    EditText editEmail;
    RadioGroup radioGroup;
    // 라디오버튼 두가지는 여기서 가져올 필요가 없다.
    Button btnSend;

    Person person;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        radioGroup = findViewById(R.id.radioGruop);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                // 1. isMale(boolean) 정보를 보내기 위해서
                    // 1-1. ★일단 라디오 그룹 안에서 "체크된" 버튼의 "아이디"를 가져온다★ (아이디값은 int 이므로 int 형으로 선언한다)
                int checkedId = radioGroup.getCheckedRadioButtonId();
                // 1-2. 라디오그룹 안에서 무엇이 체크되어있는지를 "라디오그룹"한테 물어본다
                    // 일단 male 을 false 처리해놓고, 경우(if)에 따라 male 값을 true 나 false 로 초기화한다
                    // 아무것도 선택을 안하면 id 정보가 "null" 로 간다.
                        // null 을 막기 위해서는
                        // xml 에서 버튼들 중 하나가 미리 선택되어있도록 설정(checkedButton)해주거나
                        // java 파일에서 버튼들 중 하나를 가져와서 버튼변수.setSelected()로 설정해준다.
                        // 혹은 여기에서 null 이면 선택하라고 경고 띄우고 리턴해주는 코드를 쓴다.
                boolean isMale = false;
                if(checkedId==R.id.radioM){
                    isMale = true;
                }else{
                    isMale = false;
                }
                // 정보들을 객체에 저장
                person = new Person(name,email,isMale);
                // 위의 객체를 새로운 액티비티에 전달
                i = new Intent(MainActivity.this,Second.class);
                // 정보를 저장한 클래스에 implements Serializable 를 추가하면
                // putExtra 에 위 클래스(객체)의 참조변수를 통째로 보낼 수 있게 된다.
                i.putExtra("PersonClass", person);
                startActivity(i);


            }
        });

    }
}