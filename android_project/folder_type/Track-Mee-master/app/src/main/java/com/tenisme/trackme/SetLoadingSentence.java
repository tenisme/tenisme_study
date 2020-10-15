package com.tenisme.trackme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tenisme.trackme.adapter.RVA_Sentence;
import com.tenisme.trackme.data.DatabaseHandler;
import com.tenisme.trackme.model.Sentence;

import java.util.ArrayList;

public class SetLoadingSentence extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView_sentence;
    Button btnAddSentence;
    EditText editAddSentence;

    RVA_Sentence rva_sentence;
    DatabaseHandler dh;
    AlertDialog dialog;
    Intent i;

    ArrayList<Sentence> sentenceArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_loading_sentence);

        btnAddSentence = findViewById(R.id.btnAddSentence);
        recyclerView_sentence = findViewById(R.id.recyclerView_sentence);

        recyclerView_sentence.setHasFixedSize(true);
        recyclerView_sentence.setLayoutManager(new LinearLayoutManager(SetLoadingSentence.this));

        dh = new DatabaseHandler(SetLoadingSentence.this);
        sentenceArrayList = dh.getAllSentences();

        rva_sentence = new RVA_Sentence(SetLoadingSentence.this, sentenceArrayList);
        recyclerView_sentence.setAdapter(rva_sentence);

        btnAddSentence.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == btnAddSentence){
            setAddSentenceDialog();
        }
    }

    public void setAddSentenceDialog(){
        AlertDialog.Builder addSentenceDialog = new AlertDialog.Builder(SetLoadingSentence.this);
        View addSentenceView = getLayoutInflater().inflate(R.layout.add_sentence_dialog, null);

        editAddSentence = addSentenceView.findViewById(R.id.editAddSentence);

        addSentenceDialog.setPositiveButton("문구 추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String addSentence = editAddSentence.getText().toString().trim();

                if(addSentence.isEmpty()){
                    Toast.makeText(SetLoadingSentence.this, "등록할 문구 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Sentence sentence = new Sentence(addSentence);
                sentenceArrayList.add(sentence);

                dh = new DatabaseHandler(SetLoadingSentence.this);
                dh.addSentence(sentence);

                dh = new DatabaseHandler(SetLoadingSentence.this);
                sentenceArrayList = dh.getAllSentences();

                rva_sentence = new RVA_Sentence(SetLoadingSentence.this, sentenceArrayList);
                rva_sentence.notifyDataSetChanged();

                Toast.makeText(SetLoadingSentence.this, "문구 등록 성공!", Toast.LENGTH_SHORT).show();
            }
        });
        addSentenceDialog.setNegativeButton("취소", null);

        addSentenceDialog.setView(addSentenceView);

        dialog = addSentenceDialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(SetLoadingSentence.this, Loading.class);
        startActivity(i);
        finish();
    }
}