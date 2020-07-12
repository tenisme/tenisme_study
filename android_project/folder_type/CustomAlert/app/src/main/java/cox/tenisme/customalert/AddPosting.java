package cox.tenisme.customalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import cox.tenisme.customalert.adapter.RecyclerViewAdapter;
import cox.tenisme.customalert.model.Post;

public class AddPosting extends AppCompatActivity {

    Intent i;

    EditText editTitle;
    EditText editPost;
    Button btnSave;

    Post posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 1. ★툴바 왼쪽에 백버튼이 생김. 액션은 별개임.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 다른 액티비티에 앱바 설정하는 과정
        // (1) activity_main.xml 에 있는 앱바레이아웃, 툴바 설정을 복사해서 이 액티비티와 연결된 .xml 파일에 붙여넣기
        // (2) 앱바 만들 액티비티에서 Toolbar toolbar = findViewById(R.id.toolbar);
            // setSupportActionBar(toolbar); 설정
        // (3) 매니페스트 파일에 등록된 이 액티비티에 android:theme="@style/AppTheme.NoActionBar" 설정 추가
        // (4) 없이도 됨. ^.^.. 위에 세 단계 다 셋팅하지 않고 onCreateOptionsMenu() 여기서
            // 메뉴 주소(R.menu.~, menu) 설정을 이 액티비티 전용 menu_~.xml 로 바꿔주면 됨.

        editTitle = findViewById(R.id.editTitle);
        editPost = findViewById(R.id.editPost);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTitle.getText().toString().trim();
                String post = editPost.getText().toString().trim();

                if(title.isEmpty() || post.isEmpty()){
                    Toast.makeText(AddPosting.this,"데이터를 입력하세요",Toast.LENGTH_SHORT).show();
                    return; // 리턴 까먹지마!!!!!!!!!!!!!!!!!!!!!!11
                }

                i = getIntent();
                posts = new Post(title,post);
                i.putExtra("PostClass",posts);
                setResult(RESULT_OK, i);

                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_posting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 2. ★왼쪽에 만든 백버튼의 액션은 여기서 설정한다
            // 왼쪽 백버튼 아이디 > android.R.id.home
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.menu_back) {
            i = getIntent();
            setResult(RESULT_CANCELED, i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}