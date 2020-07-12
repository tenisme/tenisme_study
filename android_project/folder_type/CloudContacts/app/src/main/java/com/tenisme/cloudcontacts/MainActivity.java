package com.tenisme.cloudcontacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tenisme.cloudcontacts.adapter.RecyclerViewAdapter;
import com.tenisme.cloudcontacts.data.Contact;
import com.tenisme.cloudcontacts.util.Util;

import java.util.ArrayList;

import static com.tenisme.cloudcontacts.util.Util.KEY_COLLECTION;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button btnAdd;
    RecyclerView recyclerView;

    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Contact> contacts = new ArrayList<>();
    Contact contact;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,AddContact.class);
                startActivity(intent);
            }
        });
    }

    public void loadData(){
        db.collection(Util.KEY_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // 여러개의 도큐먼트 가져오기
                        for(QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
//                            String id = snapshots.getId(); // id 가져오기
//                            Log.i("AAA", ""+id);
                            contact = snapshots.toObject(Contact.class); // Journal Class 로 가져와라.
                            contacts.add(contact);
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,contacts);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        loadData();
    }

}