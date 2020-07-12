package cox.tenisme.customalert.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cox.tenisme.customalert.MainActivity;
import cox.tenisme.customalert.R;
import cox.tenisme.customalert.model.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Post> postArrayList;

    RecyclerViewAdapter recyclerViewAdapter;

    public RecyclerViewAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Post post = postArrayList.get(position);

        String title = post.getTitle();
        String body = post.getBody();

        holder.txtTitle.setText(title);
        holder.txtBody.setText("    "+body);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtBody;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.editTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("포스트 삭제");
                    alert.setMessage("정말 삭제하시겠습니까?");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int index = getAdapterPosition();
                            postArrayList.remove(index);

//                            recyclerViewAdapter = new RecyclerViewAdapter(context, postArrayList);
                            // 위에 코드 필요없다. 이 어레이리스트가 이미 어댑터에 들어있기 때문이다. 아래처럼 어댑터 갱신만 해주면 된다.
                            notifyDataSetChanged();
                        }
                    });
                    alert.setNegativeButton("No", null);
                    alert.setCancelable(false);
                    alert.show();

                }
            });
        }
    }
}
