package com.tenisme.searchplace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tenisme.searchplace.adapter.RecyclerViewAdapter_SavePlace;
import com.tenisme.searchplace.data.DatabaseHandler;
import com.tenisme.searchplace.model.SearchPlace;
import com.tenisme.searchstore.R;

import java.util.ArrayList;

public class SavePlace extends AppCompatActivity {

    RecyclerView saveRecyclerView;
    RecyclerViewAdapter_SavePlace recyclerViewAdapterSavePlace;
    ArrayList<SearchPlace> saveSearchPlaces = new ArrayList<>();
    SearchPlace searchPlace;

    DatabaseHandler dh;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_place);

        saveRecyclerView = findViewById(R.id.saveRecyclerView);
        saveRecyclerView.setHasFixedSize(true);
        saveRecyclerView.setLayoutManager(new LinearLayoutManager(SavePlace.this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dh = new DatabaseHandler(SavePlace.this);
        saveSearchPlaces = dh.getAllPlace();

        recyclerViewAdapterSavePlace = new RecyclerViewAdapter_SavePlace(SavePlace.this,saveSearchPlaces);
        saveRecyclerView.setAdapter(recyclerViewAdapterSavePlace);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_data, menu);
        return true;
    }

    // 액션바 버튼 셋팅
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 버튼 아이템 빼내는 코드
        int id = item.getItemId();

        // 바로 아래는 버튼의 액션 설정
        if(id == android.R.id.home){
            finish();
            return true;
        }
        if (id == R.id.menu_delete_all) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SavePlace.this);
            alertDialog.setMessage("장소를 전부 삭제하시겠습니까?");
            alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for(int i = 0 ; i < saveSearchPlaces.size() ; i++) {
                       searchPlace = saveSearchPlaces.get(i);
                       dh.deletePlace(searchPlace);
                    }
                    saveSearchPlaces = dh.getAllPlace();

                    recyclerViewAdapterSavePlace = new RecyclerViewAdapter_SavePlace(SavePlace.this,saveSearchPlaces);
                    saveRecyclerView.setAdapter(recyclerViewAdapterSavePlace);

                    Toast.makeText(SavePlace.this,"전부 삭제되었습니다",Toast.LENGTH_SHORT).show();

                }
            });
            alertDialog.setNegativeButton("아니오", null);
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        i = getIntent();
//        i.putExtra("saved", 0);
//        setResult(RESULT_OK, i);
//    }
}