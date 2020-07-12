package cox.tenisme.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import cox.tenisme.contactmanager.data.DatabaseHandler;
import cox.tenisme.contactmanager.model.Contact;

public class UpdateContact extends AppCompatActivity {

    EditText editCorrectName;
    EditText editCorrectPhoneNumber;
    Button btnCorrectSave;

    DatabaseHandler dh;
    Contact contact;
    ArrayList<Contact> contactArrayList;

    Intent i;
    int selectPosition;

    String name;
    String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_contact);

        editCorrectName = findViewById(R.id.editCorrectName);
        editCorrectPhoneNumber = findViewById(R.id.editCorrectPhoneNumber);
        btnCorrectSave = findViewById(R.id.btnCorrectSave);

        dh = new DatabaseHandler(UpdateContact.this);
        contactArrayList = dh.getAllContacts();

        i = getIntent();
        selectPosition = i.getIntExtra("selectPosition",-1);
            // 인덱스는 0부터 시작하므로 디폴트(널)값은 0이 아닌 -1을 지정해주는 것이 좋다.

        contact = contactArrayList.get(selectPosition);
        name = contact.getName();
        phoneNumber = contact.getPhoneNumber();

        editCorrectName.setText(name);
        editCorrectPhoneNumber.setText(phoneNumber);

        btnCorrectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correctName = editCorrectName.getText().toString().trim();
                String correctPhone = editCorrectPhoneNumber.getText().toString().trim();

                contact = contactArrayList.get(selectPosition);
                contact.setName(correctName);
                contact.setPhoneNumber(correctPhone);
                dh.updateContact(contact);

                Toast.makeText(UpdateContact.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
