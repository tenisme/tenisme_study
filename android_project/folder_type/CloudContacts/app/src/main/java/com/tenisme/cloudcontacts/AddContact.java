package com.tenisme.cloudcontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tenisme.cloudcontacts.data.Contact;
import com.tenisme.cloudcontacts.util.Util;

import static com.tenisme.cloudcontacts.util.Util.KEY_COLLECTION;

public class AddContact extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editSaveName;
    EditText editSavePhoneNumber;
    Button btnSave;

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editSaveName = findViewById(R.id.editSaveName);
        editSavePhoneNumber = findViewById(R.id.editSavePhoneNumber);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editSaveName.getText().toString().trim();
                String phoneNumber = editSavePhoneNumber.getText().toString().trim();

                if(name.isEmpty() || phoneNumber.isEmpty()){
                    Toast.makeText(AddContact.this,"이름과 번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }

                contact = new Contact(name, phoneNumber);

                db.collection(Util.KEY_COLLECTION)
                        .add(contact)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();
                                contact.setId(id);

                                db.collection(Util.KEY_COLLECTION)
                                        .document(documentReference.getId()).set(contact);

                                Toast.makeText(AddContact.this,"저장에 성공했습니다",Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddContact.this,"저장에 실패했습니다",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}