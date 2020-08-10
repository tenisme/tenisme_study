package com.tenisme.movieapp.adapter;

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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.movieapp.R;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movieArrayList;

    Movie movie;
    int movie_id;

    ImageView img_favorite;

    String query = "";
    String token = "";

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
                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                    token = sharedPreferences.getString("token", null);

                    if (token == null) {
                        Toast.makeText(context, "영화를 즐겨찾기에 추가하려면 로그인이 필요합니다", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // favorite 추가/취소하기
                    JSONObject object = new JSONObject();
                    try {
                        object.put("movie_id", movie_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    request(Request.Method.POST, "/api/v1/favorites", object);


                }
            });

        }
    }

    public void request(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Movie_app", Utils.BASE_URL + api_url + query);

                        boolean success = false;

                        try {
                            success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(success){
                            // 즐겨찾기가 잘 등록되었을 경우
                            img_favorite.setImageResource(android.R.drawable.btn_star_big_on);
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
