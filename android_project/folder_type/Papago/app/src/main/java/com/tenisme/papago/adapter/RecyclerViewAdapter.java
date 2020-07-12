package com.tenisme.papago.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tenisme.papago.R;
import com.tenisme.papago.data.DatabaseHandler;
import com.tenisme.papago.model.Translations;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Translations> translationsArrayList;
    Translations translations;

    public RecyclerViewAdapter(Context context, ArrayList<Translations> papagoArrayList) {
        this.context = context;
        this.translationsArrayList = papagoArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        translations = translationsArrayList.get(position);
        String trans = translations.getTrans();
        String before = translations.getBefore();
        String after = translations.getAfter();

        holder.txtTrans.setText(trans);
        holder.txtBefore.setText(before);
        holder.txtAfter.setText(after);
    }

    @Override
    public int getItemCount() {
        return translationsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txtTrans;
        TextView txtBefore;
        TextView txtAfter;
        Button btnDelete;

        TextView txtTransAlert;
        TextView txtBeforeAlert;
        TextView txtAfterAlert;

        DatabaseHandler dh;

        AlertDialog dialog;
        AlertDialog.Builder alert;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtTrans = itemView.findViewById(R.id.txtTrans);
            txtBefore = itemView.findViewById(R.id.txtBefore);
            txtAfter = itemView.findViewById(R.id.txtAfter);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert = new AlertDialog.Builder(context);

                    View alertView = View.inflate(context,R.layout.alert_dialog,null);

                    txtTransAlert = alertView.findViewById(R.id.txtTransAlert);
                    txtBeforeAlert = alertView.findViewById(R.id.txtBeforeAlert);
                    txtAfterAlert = alertView.findViewById(R.id.txtAfterAlert);

                    translations = translationsArrayList.get(getAdapterPosition());
                    String trans = translations.getTrans();
                    String before = translations.getBefore();
                    String after = translations.getAfter();

                    txtTransAlert.setText(trans);
                    txtBeforeAlert.setText(before);
                    txtAfterAlert.setText(after);

                    alert.setPositiveButton("뒤로 가기", null);
                    alert.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setMessage("삭제하시겠습니까?");
                            alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh = new DatabaseHandler(context);
                                    translationsArrayList = dh.getAllTrans();
                                    translations = translationsArrayList.get(getAdapterPosition());
                                    dh.deleteTrans(translations);

                                    translationsArrayList = dh.getAllTrans();
                                    notifyDataSetChanged();
                                }
                            });
                            alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // todo : 바로 메인으로 안 가고 이전 알러트로 가고싶은디..
                                }
                            });
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    });

                    alert.setView(alertView);
                    alert.setCancelable(false);

                    dialog = alert.create();
                    dialog.show();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("삭제하시겠습니까?");
                    alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dh = new DatabaseHandler(context);
                            translationsArrayList = dh.getAllTrans();
                            translations = translationsArrayList.get(getAdapterPosition());
                            dh.deleteTrans(translations);

                            translationsArrayList = dh.getAllTrans();
//                            recyclerViewAdapter = new RecyclerViewAdapter(context,translationsArrayList);
                                // 위에거 필요없음. 아래의 함수 자체가 어댑터를 포함하고 있기 때문에 굳이 "어댑터"를 새로 갱신해줄 필요는 없음.
                            notifyDataSetChanged();
                        }
                    });
                    alertDialog.setNegativeButton("아니오", null);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            });
        }
    }
}
