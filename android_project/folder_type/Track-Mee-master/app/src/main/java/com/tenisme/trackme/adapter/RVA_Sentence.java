package com.tenisme.trackme.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tenisme.trackme.R;
import com.tenisme.trackme.SetLoadingSentence;
import com.tenisme.trackme.data.DatabaseHandler;
import com.tenisme.trackme.model.Sentence;
import com.tenisme.trackme.util.Utils;

import java.util.ArrayList;

public class RVA_Sentence extends RecyclerView.Adapter<RVA_Sentence.ViewHolder> {

    Context context;
    ArrayList<Sentence> sentenceArrayList;

    DatabaseHandler dh;

    Sentence sentence;

    String log = Utils.LOG;

    public RVA_Sentence(Context context, ArrayList<Sentence> sentenceArrayList) {
        this.context = context;
        this.sentenceArrayList = sentenceArrayList;
    }

    @NonNull
    @Override
    public RVA_Sentence.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sentences, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVA_Sentence.ViewHolder holder, int position) {
        Sentence getSentence = sentenceArrayList.get(position);

        String sentence = getSentence.getSentence();

        holder.txtSentence.setText(sentence);
    }

    @Override
    public int getItemCount() {
        return sentenceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSentence;
        Button btnEditSentence;
        Button btnDeleteSentence;

        EditText editUpdateSentence;

        AlertDialog dialog;

        String getSentence;
        String updateSentence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSentence = itemView.findViewById(R.id.txtSentence);
            btnEditSentence = itemView.findViewById(R.id.btnEditSentence);
            btnDeleteSentence = itemView.findViewById(R.id.btnDeleteSentence);

            btnEditSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder updateSentenceDialog = new AlertDialog.Builder(context);
                    View updateSentenceView = ((SetLoadingSentence)context).getLayoutInflater()
                            .inflate(R.layout.update_sentence_dialog, null);

                    editUpdateSentence = updateSentenceView.findViewById(R.id.editUpdateSentence);

                    // 기존 문구 내용을 불러와서 EditText 에 배치
                    dh = new DatabaseHandler(context);
                    sentenceArrayList = dh.getAllSentences();
                    sentence = sentenceArrayList.get(getAdapterPosition());
                    getSentence = sentence.getSentence();
                    editUpdateSentence.setText(getSentence);

                    updateSentenceDialog.setPositiveButton("문구 수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateSentence = editUpdateSentence.getText().toString().trim();
                            Log.i(log, "get updateSentence");

                            // 내용이 없으면 기존 내용으로 다시 저장
                            if(updateSentence.isEmpty()){
                                updateSentence = getSentence;
                            }

                            // 어레이리스트에서 불러온 해당 포지션의 sentence 를 업데이트한 문구로 저장
                            sentence.setSentence(updateSentence);
                            Log.i(log, "update sentence");

                            // 어레이리스트 갱신
                            sentenceArrayList.set(getAdapterPosition(),sentence);
                            Log.i(log, "update sentence on array");
                            // DB 갱신
                            dh.updateSentence(sentence);
                            Log.i(log, "update sentence on DB");

                            // 여기서 다시 sentenceArrayList = dh.getAllSentences()를 했다가
                            // 오류가 떴었는데 위에서 update 하고나서 마지막에 DB가 닫혀서 생긴 문제같음.
                            // DB를 호출하는 sentenceArrayList = dh.getAllSentences() 이 코드를 지우니까 잘 갱신됨.

                            // 리사이클러뷰 갱신
                            notifyDataSetChanged();
                            Log.i(log, "notifyDataSetChanged()");

                            Toast.makeText(context, "문구 수정 성공!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    updateSentenceDialog.setNegativeButton("취소", null);

                    updateSentenceDialog.setView(updateSentenceView);

                    dialog = updateSentenceDialog.create();
                    dialog.show();
                }
            });

            btnDeleteSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder deleteSentenceDialog = new AlertDialog.Builder(context);
                    deleteSentenceDialog.setTitle("문구 삭제");
                    deleteSentenceDialog.setMessage("이 문구를 삭제하시겠습니까?");
                    deleteSentenceDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dh = new DatabaseHandler(context);
                            sentenceArrayList = dh.getAllSentences();
                            sentence = sentenceArrayList.get(getAdapterPosition());

                            dh.deleteSentence(sentence);

                            dh = new DatabaseHandler(context);
                            sentenceArrayList = dh.getAllSentences();

                            notifyDataSetChanged();

                            Toast.makeText(context, "선택한 문구가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                    deleteSentenceDialog.setNegativeButton("취소",null); // 아직 설명 없음
                    deleteSentenceDialog.show();
                }
            });
        }
    }
}
