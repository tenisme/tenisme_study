package com.tenisme.movieapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.movieapp.Favorite;
import com.tenisme.movieapp.MainActivity;
import com.tenisme.movieapp.R;
import com.tenisme.movieapp.Welcome;
import com.tenisme.movieapp.model.Movie;
import com.tenisme.movieapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapterMain extends RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder> {

    Context context;
    ArrayList<Movie> movieArrayList;

    Movie movie;
    int movie_id;

    ImageView img_favorite;

    String query = "";
    String token = "";

    public RecyclerViewAdapterMain(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMain.ViewHolder holder, int position) {
        movie = movieArrayList.get(position);

        movie_id = movie.getMovie_id();
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

        holder.txt_title.setText(title);
        holder.txt_genre.setText(genre);
        holder.txt_attnd.setText(attendance);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
                    SharedPreferences sharedPreferences = v.getContext()
                            .getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                    token = sharedPreferences.getString("token", null);

                    if (token == null) {
                        Toast.makeText(context, "영화를 즐겨찾기에 추가하려면 로그인이 필요합니다", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // favorite 추가/취소 셋팅 : movie_id 가져오기
                    JSONObject object = new JSONObject();
                    try {
                        object.put("movie_id", movie_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // 이미 즐겨찾기에 등록한 movie_id 인지 아니면 없는 movie_id 인지 판단
                    favoriteRequest(Request.Method.GET, "/api/v1/favorites/search", object);
                }
            });

        }
    }

    public void favoriteRequest(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Movie_app", Utils.BASE_URL + api_url + query);

                        try {
                            boolean success = response.getBoolean("success");

                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("즐겨찾기에 추가하시겠습니까?");
                                builder.setPositiveButton("예",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO : 즐겨찾기 추가 api 실행
                                            }
                                        });
                                builder.setNegativeButton("아니오", null);
                                builder.show();
                                Toast.makeText(context,"선택한 영화가 즐겨찾기에 추가되었습니다",Toast.LENGTH_SHORT).show();
                                img_favorite.setImageResource(android.R.drawable.btn_star_big_on);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("즐겨찾기를 취소하시겠습니까?");
                                builder.setPositiveButton("예",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO : 즐겨찾기 삭제 api 실행
                                            }
                                        });
                                builder.setNegativeButton("아니오", null);
                                builder.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Movie_app", "ERROR : " + error.toString());
                        String json = null;

                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    Log.i("Movie_app", "ERROR : " + response.toString());
                                    // 1062(이미 존재하는 즐겨찾기일 경우)를 뽑아와서 작성
                                    break;
                            }
                            //Additional cases
                        }
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
