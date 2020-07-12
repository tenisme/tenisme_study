package com.tenisme.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tenisme.firestore.model.Journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //널포인트익셉션 방지 미리 초기화

    EditText editTitle;
    EditText editThought;
    Button btnSave;
    TextView txtTitle;
    TextView txtThought;
    Button btnLoad;
    Button btnDelete;

    ArrayList<Journal> journals = new ArrayList<>();

    // 키값을 멤버변수로 설정(상수)
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTitle = findViewById(R.id.editTitle);
        editThought = findViewById(R.id.editThought);
        btnSave = findViewById(R.id.btnSave);
        txtTitle = findViewById(R.id.txtTitle);
        txtThought = findViewById(R.id.txtThought);
        btnLoad = findViewById(R.id.btnLoad);
        btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTitle.getText().toString().trim();
                String thought = editThought.getText().toString().trim();

                // 2. model class 를 만들어서 값을 DB에 저장하는 방법
                Journal journal = new Journal(title, thought);

                // model class 의 변수값을 document "없이" 저장하는 방법
                    // document 아이디값 없이 collection 에 값을 저장하면, 값을 저장할 때마다 자동으로 새로운 document 아이디값을 만들어서 저장한다.
                db.collection("Journal")
                        .add(journal)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                editTitle.setText("");
                                editThought.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                // 1. model class 없이 따로따로 값을 만들어 저장하는 방법
//                Map<String, Object> data = new HashMap<>();
//                data.put(KEY_TITLE, title);
//                data.put(KEY_THOUGHT, thought);

//                // Journal 컬렉션 안의 First Thoughts 도큐먼트에 data 변수값을 "저장"하는 방법 (1)
//                    // 업데이트는 그냥 같은 콜렉션에 같은 도큐멘트에 저장하면 업데이트됨.
//                db.collection("Journal") // collection 의 ★아이디값★
//                        .document("First Thoughts") // document 의 ★아이디값★
//                        .set(journal) // 여기에 Map<String, Object> 의 변수, 혹은 model Class 의 변수를 입력한다.
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // 성공시 액션
//                                Toast.makeText(MainActivity.this, "잘 저장되었습니다",
//                                        Toast.LENGTH_SHORT).show();
//                                editTitle.setText("");
//                                editThought.setText("");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // 실패시 액션
//                                Log.i("AAA", e.toString());
//                            }
//                        });
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 값 가져오기 2
                db.collection("Journal")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // 여러개의 도큐먼트 가져오기
                                for(QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                                    String id = snapshots.getId(); // id 가져오기
                                    Log.i("AAA", ""+id);
                                    Journal journal = snapshots.toObject(Journal.class); // Journal Class 로 가져와라.
                                    journals.add(journal);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                // 값 가져오기 1
//                db.collection("Journal")
//                        .document("Second Thoughts")
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                                // 키값을 가져와서 로딩한다
//                                txtTitle.setText(documentSnapshot.getString(KEY_TITLE));
//                                txtThought.setText(documentSnapshot.getString(KEY_THOUGHT));
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.i("AAA", e.toString());
//                            }
//                        });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Journal")
                        .document("QPYbWMkNgDKPRRe2GOQU")
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "삭제되었습니다",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("AAA", e.toString());
                            }
                        });
            }
        });
    }
}