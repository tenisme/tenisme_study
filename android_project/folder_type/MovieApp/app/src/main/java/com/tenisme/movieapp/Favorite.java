package com.tenisme.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.movieapp.adapter.RecyclerViewAdapterFavorite;
import com.tenisme.movieapp.model.Movie;
import com.tenisme.movieapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favorite extends AppCompatActivity {

    Movie movie;
    ArrayList<Movie> movieArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapterFavorite recyclerViewAdapterFavorite;
    int endSearch;

    int offset = 0;
    int limit = 25;
    String query = "";
    JSONArray itemArray;

    boolean success = false;
    int cnt = 0;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Log.i("Movie_app", "Start onCreate on Favorite");

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        // 리사이클러뷰 셋팅
        recyclerView = findViewById(R.id.recyclerViewFavorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Favorite.this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 리스트의 끝에 도달하면 아래를 실행하지 않고 리턴
                if(endSearch == 1){
                    return;
                }

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if (lastPosition + 1 == totalCount) {
                    // 아이템 추가! 입맛에 맞게 설정하시면됩니다.
                    query = "?offset=" + offset + "&limit=" + limit;
                    addRequest(Request.Method.GET, "/api/v1/favorites", null);
                }
            }
        });

        query = "?offset=" + offset + "&limit=" + limit;
        getRequest(Request.Method.GET, "/api/v1/favorites", null);

    }

    // Request.Method.GET = 0, POST = 1, PUT = 2, DELETE = 3, HEAD = 4
    public void getRequest(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            success = response.getBoolean("success");
                            cnt = response.getInt("cnt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Movie_app", Utils.BASE_URL + api_url + query);
                        Log.i("Movie_app", "success : " + success + ", cnt : " + cnt);

                        try {
                            itemArray = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < itemArray.length(); i++) {
                            try {
                                JSONObject objectInItems = itemArray.getJSONObject(i);
                                int id = objectInItems.getInt("id");
                                String title = objectInItems.getString("title");
                                String genre = objectInItems.getString("genre");
                                int attendance = objectInItems.getInt("attendance");
                                String year = objectInItems.getString("year");

                                movie = new Movie(id, title, genre, attendance, year, 0);
                                movieArrayList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapterFavorite = new RecyclerViewAdapterFavorite(Favorite.this, movieArrayList);
                        recyclerView.setAdapter(recyclerViewAdapterFavorite);

                        offset = offset + cnt;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Movie_app", "ERROR : " + error.toString());
                    }
                }
        );
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
        Volley.newRequestQueue(Favorite.this).add(jsonObjectRequest);
    }

    public void addRequest(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            success = response.getBoolean("success");
                            cnt = response.getInt("cnt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Movie_app", Utils.BASE_URL + api_url + query);
                        Log.i("Movie_app", "success : " + success + ", cnt : " + cnt);

                        // cnt 가 0이면(리스트의 끝까지 왔으면) endSearch 를 ON 하고 리턴
                        if(cnt == 0){
                            endSearch = 1;
                            return;
                        }

                        try {
                            itemArray = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < itemArray.length(); i++) {
                            try {
                                JSONObject objectInItems = itemArray.getJSONObject(i);
                                int id = objectInItems.getInt("id");
                                String title = objectInItems.getString("title");
                                String genre = objectInItems.getString("genre");
                                int attendance = objectInItems.getInt("attendance");
                                String year = objectInItems.getString("year");

                                movie = new Movie(id, title, genre, attendance, year, 0);
                                movieArrayList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapterFavorite.notifyDataSetChanged();
                        Log.i("Movie_app", "offset : "+offset);

                        offset = offset + cnt;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Movie_app", "ERROR : " + error.toString());
                    }
                }
        );
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
        Volley.newRequestQueue(Favorite.this).add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerViewAdapterFavorite != null){
            recyclerViewAdapterFavorite.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Favorite.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}