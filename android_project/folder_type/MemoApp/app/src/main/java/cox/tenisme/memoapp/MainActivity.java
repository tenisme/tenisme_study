package cox.tenisme.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import cox.tenisme.memoapp.adapter.RecyclerViewAdapter;
import cox.tenisme.memoapp.data.DatabaseHandler;
import cox.tenisme.memoapp.model.Memo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editSearch;
//    Button btnQ;
//    Button btnX;
    Button btnAddMemo;
    RecyclerView recyclerView;

    Intent i;

    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Memo> memoArrayList;

    DatabaseHandler dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
//        btnQ = findViewById(R.id.btnQ);
//        btnX = findViewById(R.id.btnX);
        btnAddMemo = findViewById(R.id.btnAddMemo);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String search = editSearch.getText().toString().trim();

                dh = new DatabaseHandler(MainActivity.this);
                memoArrayList = dh.searchMemo(search);

                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, memoArrayList);
                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
        btnAddMemo.setOnClickListener(this);
//        btnQ.setOnClickListener(this);
//        btnX.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        editSearch.setText("");

        dh = new DatabaseHandler(MainActivity.this);
        memoArrayList = dh.getAllMemos();

        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, memoArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v==btnAddMemo){
            i = new Intent(MainActivity.this, AddMemo.class);
            startActivity(i);
        }
//        if(v==btnQ){
//
//            String search = editSearch.getText().toString().trim();
//            if(search.isEmpty()){
//                Toast.makeText(MainActivity.this,"검색 내용을 입력해주세요",Toast.LENGTH_SHORT).show();
//            }
//            dh = new DatabaseHandler(MainActivity.this);
//            memoArrayList = dh.searchMemo(search);
//
//            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, memoArrayList);
//            recyclerView.setAdapter(recyclerViewAdapter);
//
//            recyclerViewAdapter.notifyDataSetChanged();
//
//        }
//        if(v==btnX){
//
//            editSearch.setText("");
//
//            dh = new DatabaseHandler(MainActivity.this);
//            memoArrayList = dh.getAllMemos();
//
//            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, memoArrayList);
//            recyclerView.setAdapter(recyclerViewAdapter);
//
//            recyclerViewAdapter.notifyDataSetChanged();
//        }
    }
}