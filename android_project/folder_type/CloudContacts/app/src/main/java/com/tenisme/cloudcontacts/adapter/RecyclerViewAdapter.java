package com.tenisme.cloudcontacts.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tenisme.cloudcontacts.AddContact;
import com.tenisme.cloudcontacts.MainActivity;
import com.tenisme.cloudcontacts.R;
import com.tenisme.cloudcontacts.UpdateContact;
import com.tenisme.cloudcontacts.data.Contact;
import com.tenisme.cloudcontacts.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Contact> contacts;
    Contact contact;

    public RecyclerViewAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        contact = contacts.get(position);

        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();

        holder.txtName.setText(name);
        holder.txtPhoneNumber.setText(phoneNumber);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CardView cardView;
        TextView txtName;
        TextView txtPhoneNumber;
        ImageView imgDelete;

        Intent i;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contact = contacts.get(getAdapterPosition());

                    i = new Intent(context, UpdateContact.class);
                    i.putExtra("contact",contact);
                    context.startActivity(i);

                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder completeAlert = new AlertDialog.Builder(context);
                    completeAlert.setTitle("주소록 삭제");
                    completeAlert.setMessage("정말 삭제하시겠습니까?");
                    completeAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String id = contacts.get(getAdapterPosition()).getId();

                            db.collection(Util.KEY_COLLECTION)
                                    .document(id)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "삭제되었습니다",
                                                    Toast.LENGTH_SHORT).show();

                                            ((MainActivity)context).recreate();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("AAA", e.toString());
                                            Toast.makeText(context, "삭제에 실패했습니다",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                    completeAlert.setNegativeButton("No", null);
                    completeAlert.setCancelable(false);
                    completeAlert.show();
                }
            });
        }
    }
}
