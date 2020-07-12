package cox.tenisme.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cox.tenisme.memoapp.data.DatabaseHandler;
import cox.tenisme.memoapp.model.Memo;

public class AddMemo extends AppCompatActivity {

    EditText editTitle;
    EditText editMemo;
    Button btnMemoSave;

    DatabaseHandler dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        editTitle = findViewById(R.id.editTitle);
        editMemo = findViewById(R.id.editMemo);
        btnMemoSave = findViewById(R.id.btnMemoSave);

        btnMemoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                String memo = editMemo.getText().toString().trim();

                if(title.isEmpty() || memo.isEmpty()){
                    Toast.makeText(AddMemo.this,"제목과 내용은 필수입니다", Toast.LENGTH_SHORT).show();
                    return; // 이거 끝내고 아래 작업 실행시키고 싶지 않으면 리턴 필수!!
                }

                dh = new DatabaseHandler(AddMemo.this);
                Memo new_memo = new Memo();
                new_memo.setTitle(title);
                new_memo.setMemo(memo);
                dh.addMemo(new_memo);

                Toast.makeText(AddMemo.this,"메모가 저장되었습니다",Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }
}