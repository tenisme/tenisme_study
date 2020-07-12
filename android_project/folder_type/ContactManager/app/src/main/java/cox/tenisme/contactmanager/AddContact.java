package cox.tenisme.contactmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import cox.tenisme.contactmanager.data.DatabaseHandler;
import cox.tenisme.contactmanager.model.Contact;

public class AddContact extends AppCompatActivity {

    EditText editName;
    EditText editPhoneNumber;
    Button btnSave;

    String name;
    String phoneNumber;

    DatabaseHandler db; // db 저장용
    Contact new_contact; // 데이터 세팅용

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editName = findViewById(R.id.editName);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseHandler(AddContact.this); // 만들었으면 인스턴스 생성 좀 해주셈
                name = editName.getText().toString().trim();
                phoneNumber = editPhoneNumber.getText().toString().trim();

                if(name.isEmpty() || phoneNumber.isEmpty()){
                    Toast.makeText(AddContact.this, "이름이나 연락처는 필수입니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                new_contact = new Contact(); // 인스턴스 생성2222222222333333344444444444
                new_contact.setName(name);
                new_contact.setPhoneNumber(phoneNumber);
                db.addContact(new_contact);

                Toast.makeText(AddContact.this, "잘 저장되었습니다", Toast.LENGTH_SHORT).show();

                i = getIntent(); // 이전 액티비티로 돌아가기 위해서 getIntent
                setResult(RESULT_OK, i); // result_code 로 RESULT_OK를 보내고 i를 실행
                finish(); // 하고 종료
            }
        });
    }

}