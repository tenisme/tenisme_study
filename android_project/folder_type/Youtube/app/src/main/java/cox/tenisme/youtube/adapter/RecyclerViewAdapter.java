package cox.tenisme.youtube.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;

import java.util.ArrayList;

import cox.tenisme.youtube.BigThumbnail;
import cox.tenisme.youtube.MainActivity;
import cox.tenisme.youtube.R;
import cox.tenisme.youtube.model.Youtube;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Youtube> youtubeArrayList;

    Youtube youtube;
    Intent i;

    public RecyclerViewAdapter(Context context, ArrayList<Youtube> youtubeArrayList) {
        this.context = context;
        this.youtubeArrayList = youtubeArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        youtube = youtubeArrayList.get(position);
        String title = youtube.getTitle();
        String description = youtube.getDescription();
        String thumbnailUrl = youtube.getThumbnailUrl();

        holder.txtTitle.setText(title);
        holder.txtDescription.setText(description);
        Glide.with(context).load(thumbnailUrl).placeholder(android.R.drawable.ic_dialog_alert)
                .into(holder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return youtubeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txtTitle;
        TextView txtDescription;
        ImageView imgThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);

            imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youtube = youtubeArrayList.get(getAdapterPosition());
                    String thumbnailBigUrl = youtube.getThumbnailBigUrl();
                    i = new Intent(context, BigThumbnail.class);
                    i.putExtra("thumbnailBigUrl",thumbnailBigUrl);
                    context.startActivity(i);
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youtube = youtubeArrayList.get(getAdapterPosition());
                    String videoId = youtube.getVideoId();
                    String videoUrl = "https://www.youtube.com/watch?v="+videoId;
                    Uri uri = Uri.parse(videoUrl);
                    i = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(i);
                }
            });
        }
    }
}
