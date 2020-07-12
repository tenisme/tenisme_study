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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tenisme.searchplace.MainActivity;
import com.tenisme.searchplace.SavePlace;
import com.tenisme.searchplace.data.DatabaseHandler;
import com.tenisme.searchplace.model.SearchPlace;
import com.tenisme.searchstore.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RecyclerViewAdapter_SavePlace extends RecyclerView.Adapter<RecyclerViewAdapter_SavePlace.ViewHolder>{

    Context context;
    ArrayList<SearchPlace> saveSearchPlaces;
    SearchPlace searchPlace;

    DatabaseHandler dh;

    public RecyclerViewAdapter_SavePlace(Context context, ArrayList<SearchPlace> saveSearchPlaces) {
        this.context = context;
        this.saveSearchPlaces = saveSearchPlaces;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_SavePlace.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_data_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_SavePlace.ViewHolder holder, int position) {
        dh = new DatabaseHandler(context);
        saveSearchPlaces = dh.getAllPlace();
        searchPlace = saveSearchPlaces.get(position);
        String storeName = searchPlace.getPlaceName();
        String vicinity = searchPlace.getVicinity();

        holder.txtPlaceName2.setText(storeName);
        holder.txtVicinity2.setText(vicinity);
    }

    @Override
    public int getItemCount() {
        return saveSearchPlaces.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtSeeMap2;
        TextView txtDeletePlace;
        TextView txtPlaceName2;
        TextView txtVicinity2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSeeMap2 = itemView.findViewById(R.id.txtSeeMap2);
            txtDeletePlace = itemView.findViewById(R.id.txtDeletePlace);
            txtPlaceName2 = itemView.findViewById(R.id.txtPlaceName2);
            txtVicinity2 = itemView.findViewById(R.id.txtVicinity2);

            txtSeeMap2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            txtDeletePlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("장소를 삭제하시겠습니까?");
                    alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            searchPlace = saveSearchPlaces.get(getAdapterPosition());
//                            searchPlace.setSaved(0);

                            dh = new DatabaseHandler(context);
                            dh.deletePlace(searchPlace);
                            saveSearchPlaces = dh.getAllPlace();

                            notifyDataSetChanged();

                            Toast.makeText(context,"삭제되었습니다",Toast.LENGTH_SHORT).show();

                        }
                    });
                    alertDialog.setNegativeButton("아니오", null);
                    alertDialog.show();

                }
            });
        }
    }
}
