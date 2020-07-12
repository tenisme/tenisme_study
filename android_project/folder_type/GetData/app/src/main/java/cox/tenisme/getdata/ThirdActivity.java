package cox.tenisme.getdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtData;
    EditText editSend;
    Button btnThird;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        txtData = findViewById(R.id.txtData);
        editSend = findViewById(R.id.editSend);
        btnThird = findViewById(R.id.btnThird);

        i = getIntent();
        String data = i.getStringExtra("data");
        txtData.setText(data);

        btnThird.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnThird){
            String data = editSend.getText().toString().trim();
            i = getIntent();
            i.putExtra("thirdData", data);
            setResult(RESULT_OK, i);
            finish();
        }
    }
}