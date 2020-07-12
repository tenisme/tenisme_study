package cox.tenisme.memoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import cox.tenisme.memoapp.data.DatabaseHandler;
import cox.tenisme.memoapp.model.Memo;

public class UpdateMemo extends AppCompatActivity {

    EditText editTitle;
    EditText editMemo;
    Button btnMemoSave;

    DatabaseHandler dh;
    ArrayList<Memo> memoArrayList;
    Memo memo;

    Intent i;
    int selectPosition;

    String getTitle;
    String getMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memo);

        editTitle = findViewById(R.id.editTitle);
        editMemo = findViewById(R.id.editMemo);
        btnMemoSave = findViewById(R.id.btnMemoSave);

        dh = new DatabaseHandler(UpdateMemo.this);
        memoArrayList = dh.getAllMemos();

        i = getIntent();
        selectPosition = i.getIntExtra("selectPosition",-1);

        memo = memoArrayList.get(selectPosition);
        getTitle = memo.getTitle();
        getMemo = memo.getMemo();

        editTitle.setText(getTitle);
        editMemo.setText(getMemo);

        btnMemoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder completeAlert = new AlertDialog.Builder(UpdateMemo.this);
                completeAlert.setTitle("메모 수정");
                completeAlert.setMessage("수정을 완료하시겠습니까?");
                completeAlert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String setTitle = editTitle.getText().toString().trim();
                        String setMemo = editMemo.getText().toString().trim();

                        memo = memoArrayList.get(selectPosition);
                        memo.setTitle(setTitle);
                        memo.setMemo(setMemo);
                        dh.updateMemo(memo);

                        Toast.makeText(UpdateMemo.this, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
                completeAlert.setNegativeButton("취소", null);
                completeAlert.setCancelable(false);
                completeAlert.show();
            }
        });
    }
}