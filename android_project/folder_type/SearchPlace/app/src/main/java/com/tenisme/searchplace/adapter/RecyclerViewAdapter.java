package com.tenisme.searchplace.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tenisme.searchplace.MainActivity;
import com.tenisme.searchplace.SavePlace;
import com.tenisme.searchplace.data.DatabaseHandler;
import com.tenisme.searchplace.model.SearchPlace;
import com.tenisme.searchstore.R;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<SearchPlace> searchPlaces;
    SearchPlace searchPlace;

    public RecyclerViewAdapter(Context context, ArrayList<SearchPlace> searchStores) {
        this.context = context;
        this.searchPlaces = searchStores;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        searchPlace = searchPlaces.get(position);
        String storeName = searchPlace.getPlaceName();
        String vicinity = searchPlace.getVicinity();
//        int saved = searchPlace.getSaved();
//
//        if(saved == 1){
//            holder.txtSavePlace.setText("장소 저장됨");
//        }else if(saved == 0){
//            holder.txtSavePlace.setText("장소 저장");
//        }
        holder.txtPlaceName.setText(storeName);
        holder.txtVicinity.setText(vicinity);
    }

    @Override
    public int getItemCount() {
        return searchPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtSeeMap;
        TextView txtSavePlace;
        TextView txtPlaceName;
        TextView txtVicinity;

        DatabaseHandler dh;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            txtSeeMap = itemView.findViewById(R.id.txtSeeMap);
            txtSavePlace = itemView.findViewById(R.id.txtSavePlace);
            txtPlaceName = itemView.findViewById(R.id.txtPlaceName);
            txtVicinity = itemView.findViewById(R.id.txtVicinity);


            txtSeeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo : "지도 보기"를 클릭하면 해당 맵을 이미지로 띄워주는 AlertDialog 작성 or 맵을 띄워주는 주소를 인터넷브라우저로 연결해 보여주기(Intent)
                        // 클릭하면 일단 "지도를 보시겠습니까?"라고 알러트를 띄워주고, "예"를 누르면 위 행동을 하고, "아니오"를 누르면 뒤로 돌아간다.
                        // 참고하기 : CustomAlert / Location 프로젝트
                }
            });

            txtSavePlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchPlace = searchPlaces.get(getAdapterPosition());

                    if(searchPlace.getSaved() == 1){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setMessage("장소 저장을 취소하시겠습니까?");
                        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dh = new DatabaseHandler(context);
                                dh.deletePlace(searchPlace);

                                notifyDataSetChanged();

                                Toast.makeText(context,"저장이 취소되었습니다",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setNegativeButton("아니오", null);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }else if(searchPlace.getSaved() == 0){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setMessage("장소를 저장하시겠습니까?");
                        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                searchPlace.setSaved(1);
                                dh = new DatabaseHandler(context);

                                dh.addPlace(searchPlace);

                                notifyDataSetChanged();

                                Toast.makeText(context,"저장되었습니다",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setNegativeButton("아니오", null);
                        alertDialog.show();
                    }
                }
            });
        }

    }
}
