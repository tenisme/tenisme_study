package com.tenisme.cloudcontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

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
import com.tenisme.cloudcontacts.data.Contact;
import com.tenisme.cloudcontacts.util.Util;

public class UpdateContact extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editUpdateName;
    EditText editUpdatePhoneNumber;
    Button btnUpdate;

    Intent i;

    Contact contact;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        editUpdateName = findViewById(R.id.editUpdateName);
        editUpdatePhoneNumber = findViewById(R.id.editUpdatePhoneNumber);
        btnUpdate = findViewById(R.id.btnUpdate);

        i = getIntent();
        contact = (Contact) i.getSerializableExtra("contact");

        id = contact.getId();
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();

        editUpdateName.setText(name);
        editUpdatePhoneNumber.setText(phoneNumber);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateName = editUpdateName.getText().toString().trim();
                String updatePhoneNumber = editUpdatePhoneNumber.getText().toString().trim();

                contact = new Contact(updateName, updatePhoneNumber);

                db.collection(Util.KEY_COLLECTION)
                        .document(id)
                        .set(contact)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(UpdateContact.this,"주소록이 변경되었습니다",Toast.LENGTH_SHORT).show();

                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(UpdateContact.this,"주소록이 변경이 실패했습니다",Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }
}