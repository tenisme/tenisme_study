package cox.tenisme.sp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editMsg;
    TextView txtMsg;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMsg = findViewById(R.id.editMsg);
        txtMsg  = findViewById(R.id.txtMsg);
        btnSave = findViewById(R.id.btnSave);

        // 만약 쉐어드 프리퍼런스에 내가 저장한 msg 라는 데이터가 있다면, 텍스트뷰에 표시한다.
        SharedPreferences sp = getSharedPreferences("sp_prefs", MODE_PRIVATE); // 이 저장소를 다시 가져와라. get~
        String value = sp.getString("msg", null); // msg 에 저장한 값이 있으면 msg, 없으면 null 로 가져와라.
        if(value != null){ // 만약 이 값이 null 이 아니면(저장한 데이터가 있으면)
            txtMsg.setText(value); // 이 값을 txtMsg 에 표시하겠다
        }

        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnSave){
            // 버튼을 누르면 editMsg 에 적힌 내용을 가져와서 SharedPreference 에 저장
            // SharedPreference 란? 안드로에드에서 제공하는, 폰에 "데이터"를 저장하는 방법 중에 하나
            String data = editMsg.getText().toString().trim();
            // 이하 SharedPreference 영역에 저장하는 작업
            SharedPreferences sp = getSharedPreferences("sp_prefs", MODE_PRIVATE);
                // () 안에는 사용할 프리퍼런스 영역의 이름을 맘대로 지어준다(보통은 앱 이름을 적는다) 저장소의 이름을 정하는 것
                // MODE_PRIVATE 다른 사람들이 이 프리퍼런스를 이용하지 못하게 하겠다. 다른 앱에서는 이 앱에서 이 프리퍼런스에 저장한 내용을 쓰지 못하게 하겠다.
            SharedPreferences.Editor editor = sp.edit(); // edit() 를 editor 로 가져옴.  이 edit()로 이 프리퍼런스에 데이터를 추가할 수 있음
            editor.putString("msg", data);
                // 쉐어드 프리퍼런스에 이 데이터를 저장함. put~는 데이터 타입. msg 는 키값, data 는 넣을 데이터.
                // 같은 키값에 저장하면 덮어쓰기한다. 변수저장과 원리가 같다.
            editor.putInt("msg2", 100);
            editor.apply(); // 이걸 써야 저장됨

        }
    }
}