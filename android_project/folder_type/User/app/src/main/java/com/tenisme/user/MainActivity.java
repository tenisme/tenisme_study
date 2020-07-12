package com.tenisme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tenisme.user.model.Members;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String ID_COLLECTION = "Members";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editEmail;
    EditText editPw;
    EditText editPwCheck;
    Button btnJoin;
    Button btnLogin;

    Members member;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.editEmail);
        editPw = findViewById(R.id.editPw);
        editPwCheck = findViewById(R.id.editPwCheck);
        btnJoin = findViewById(R.id.btnJoin);
        btnLogin = findViewById(R.id.btnLogin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editEmail.getText().toString().trim();
                password = editPw.getText().toString().trim();
                String pwCheck = editPwCheck.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(MainActivity.this,"이메일 입력은 필수입니다",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.contains("@") || !email.contains(".")){
                    Toast.makeText(MainActivity.this,"@와 .을 전부 포함한\n이메일 주소를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(MainActivity.this,"비밀번호 입력은 필수입니다",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 5 || password.length() > 13){
                    Toast.makeText(MainActivity.this,"비밀번호는 5자 이상 12자 이하로 적어주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwCheck.isEmpty()){
                    Toast.makeText(MainActivity.this,"비밀번호 확인을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.compareTo(pwCheck) != 0){
                    Toast.makeText(MainActivity.this,"비밀번호 확인을 정확하게 해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection(ID_COLLECTION)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(QueryDocumentSnapshot snapshots : queryDocumentSnapshots){

                                    String emailCheck = snapshots.getString("email");

                                    if(email.compareTo(emailCheck) == 0){
                                        AlertDialog.Builder completeAlert = new AlertDialog.Builder(MainActivity.this);
                                        completeAlert.setTitle("알림");
                                        completeAlert.setMessage("이미 가입되어있는 이메일입니다");
                                        completeAlert.setPositiveButton("OK", null);
                                        completeAlert.show();
                                        return;
                                    }
                                }

                                member = new Members(email,password);

                                db.collection(ID_COLLECTION)
                                        .add(member)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                editEmail.setText("");
                                                editPw.setText("");
                                                editPwCheck.setText("");

                                                AlertDialog.Builder completeAlert = new AlertDialog.Builder(MainActivity.this);
                                                completeAlert.setTitle("알림");
                                                completeAlert.setMessage("가입이 완료되었습니다");
                                                completeAlert.setPositiveButton("OK", null);
                                                completeAlert.show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

    }
}