package cox.tenisme.serializable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cox.tenisme.serializable.model.Person;

public class Second extends AppCompatActivity {

    TextView txtPerson;

    Intent i;

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtPerson = findViewById(R.id.txtPerson);

        i = getIntent();
        // ★Intent 에 "PersonClass"로 저장한 값을 "Person 형"으로 원상복구해야한다.
            // 가져온 키값을 (Person) 으로 형변환하면 된다.
            // 저장한 "클래스"를 가져올 때는 get~Extra 사이에 Serializable 을 적어 가져오면 된다.
        person = (Person) i.getSerializableExtra("PersonClass");
        String info = person.getName()+", "+person.getEmail()+", "+person.isMale();

        txtPerson.setText(info);

    }
}