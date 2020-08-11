package com.tenisme.movieapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.tenisme.movieapp.MainActivity;
import com.tenisme.movieapp.R;
import com.tenisme.movieapp.model.Movie;
import com.tenisme.movieapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movieArrayList;

    Movie movie;

    String token = "";
    String api_url = "";
    JSONObject object;

    int alreadyAdded;
    int checkStar = 0;

    public RecyclerViewAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        movie = movieArrayList.get(position);

        String title = movie.getTitle();
        String genre = movie.getGenre();
        String attendance = String.valueOf(movie.getAttendance());

        // year 가져와서 포맷 변경하고 배치하기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC")); // 위의 시간을 UTC 로 맞춤
        try {
            Date date = df.parse(movie.getYear());
            // 이 핸드폰의 로컬 타임존으로 바꿈
            df.setTimeZone(TimeZone.getDefault());
            String openingDate = df.format(date);
            holder.txt_year.setText(openingDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (movie.getIs_favorite() == 1) {
            holder.img_favorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else if (movie.getIs_favorite() == 0) {
            holder.img_favorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.txt_title.setText(title);
        holder.txt_genre.setText(genre);
        holder.txt_attnd.setText(attendance);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_favorite;
        TextView txt_title;
        TextView txt_genre;
        TextView txt_attnd;
        TextView txt_year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_favorite = itemView.findViewById(R.id.img_favorite);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_genre = itemView.findViewById(R.id.txt_genre);
            txt_attnd = itemView.findViewById(R.id.txt_attnd);
            txt_year = itemView.findViewById(R.id.txt_year);

            img_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Movie_app", "즐겨찾기 버튼 클릭");

                    SharedPreferences sharedPreferences = context
                            .getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                    token = sharedPreferences.getString("token", null);

                    if (token == null) {
                        Toast.makeText(context, "영화를 즐겨찾기에 추가하려면 로그인이 필요합니다", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        int position = getAdapterPosition();
                        int is_favorite = movieArrayList.get(position).getIs_favorite();

                        if (is_favorite == 1) {
                            // 즐겨찾기 삭제
                            ((MainActivity)context).unFavoriteRequest(position);
                        } else if(is_favorite == 0) {
                            // 즐겨찾기 추가
                            ((MainActivity) context).addFavoriteRequest(position);
                        }
                    }

                }
            });
        }
    }
}
