package cox.tenisme.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import cox.tenisme.photos.FloatImageActivity;
import cox.tenisme.photos.R;
import cox.tenisme.photos.model.Photos;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;
    ArrayList<Photos> photosArrayList;

    Photos photos;
    Intent i;

    public RecyclerViewAdapter(Context context, ArrayList<Photos> photosArrayList) {
        this.context = context;
        this.photosArrayList = photosArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photos_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Photos photos = photosArrayList.get(position);
        String title = photos.getTitle();
        int id = photos.getId();
        int albumId = photos.getAlbumId();
        String thumbNail = photos.getThumbnailUrl();

        // GlideUrl : 이미지에 확장자가 붙지 않았을 때 url 이미지를 가져오는 방법
            // 헤더(-H) : 승인이 필요할 때??? 주고받는 Client id와 key
        GlideUrl glideUrl = new GlideUrl(thumbNail, new LazyHeaders.Builder()
                .addHeader("User-Agent","Your-User-Agent").build());

        holder.txtTitle.setText(title);
        holder.txtId.setText(""+id);
        holder.txtAlbumId.setText(""+albumId);
        Glide.with(context).load(glideUrl).optionalCenterInside()
                .placeholder(android.R.drawable.ic_menu_help).into(holder.imgThumbnail);

    }

    @Override
    public int getItemCount() {
        return photosArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtId;
        TextView txtAlbumId;
        ImageView imgThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtId = itemView.findViewById(R.id.txtId);
            txtAlbumId = itemView.findViewById(R.id.txtAlbumId);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);

            imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    photos = photosArrayList.get(getAdapterPosition());

                    String url = photos.getUrl();

                    i = new Intent(context, FloatImageActivity.class);
                    i.putExtra("goToBig",url);
                    context.startActivity(i);
                }
            });

        }
    }
}
