package com.tenisme.places.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tenisme.places.MapsActivity;
import com.tenisme.places.R;
import com.tenisme.places.model.Store;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Store> stores = new ArrayList<>();
    Store store;

    public RecyclerViewAdapter(Context context, ArrayList<Store> stores) {
        this.context = context;
        this.stores = stores;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        store = stores.get(position);
        String storeName = store.getName();
        String address = store.getAddr();

        holder.txtName.setText(storeName);
        holder.txtAddress.setText(address);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtName;
        TextView txtAddress;

        Intent i;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtName  = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    store = stores.get(getAdapterPosition());
                    i = new Intent(context, MapsActivity.class);
                    i.putExtra("StoreClass",store);
                    context.startActivity(i);
                }
            });

        }
    }
}
