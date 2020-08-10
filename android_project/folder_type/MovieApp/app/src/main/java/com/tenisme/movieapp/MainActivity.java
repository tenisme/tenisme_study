package com.tenisme.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.movieapp.adapter.RecyclerViewAdapter;
import com.tenisme.movieapp.model.Movie;
import com.tenisme.movieapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    Movie movie;
    ArrayList<Movie> movieArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    int searchOn = 0;
    int endSearch;

    EditText edit_search;
    Button btn_search;
    Button btn_by_year;
    Button btn_by_attnd;

    int offset;
    int limit = 25;
    String search;
    int order = 1; // 1 = desc, 0 = asc
    int addOrder;
    String query = "";
    JSONArray itemArray;

    boolean success = false;
    int cnt;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그아웃을 위한 토큰 셋팅
        sharedPreferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        // 리사이클러뷰 셋팅
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
                    switch(searchOn){
                        case 1 :
                            query = "?offset=" + offset + "&limit=" + limit;
                            addRequest(Request.Method.GET, "/api/v1/movies", null);
                            break;
                        case 2 :
                            query = "?offset=" + offset + "&limit=" + limit + "&keyword=" + search;
                            addRequest(Request.Method.GET, "/api/v1/movies/search", null);
                            break;
                        case 3 :
                            query = "?offset=" + offset + "&limit=" + limit + "&order=" + addOrder + "&keyword=" + search;
                            addRequest(Request.Method.GET, "/api/v1/movies/year", null);
                            break;
                        case 4 :
                            query = "?offset=" + offset + "&limit=" + limit + "&order=" + addOrder + "&keyword=" + search;
                            addRequest(Request.Method.GET, "/api/v1/movies/attnd", null);
                            break;
                    }
                }
            }
        });

        edit_search = findViewById(R.id.edit_search);
        btn_search = findViewById(R.id.btn_search);
        btn_by_year = findViewById(R.id.btn_by_year);
        btn_by_attnd = findViewById(R.id.btn_by_attnd);

        btn_search.setOnClickListener(this);
        btn_by_year.setOnClickListener(this);
        btn_by_attnd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // search 값 가져오기
        search = edit_search.getText().toString().trim();

        // offset 디폴트 값으로 초기화
        offset = 0;

        // endSearch 디폴트값으로 초기화
        endSearch = 0;

        // 버튼 클릭 : 검색
        if (v == btn_search) {
            Log.i("Movie_app", "버튼 클릭 : 검색");

            // 버튼 누를 때마다 리스트 초기화
            if (movieArrayList.size() > 0) {
                Log.i("Movie_app", "리스트 초기화");
                movieArrayList.clear();
            }

            // 아무 검색어도 입력하지 않으면 모든 영화를 movie_id 순으로 가져옴
            if (search.isEmpty()) {
                Log.i("Movie_app", "검색어 미입력");
                query = "?offset=" + offset + "&limit=" + limit;
                getRequest(Request.Method.GET, "/api/v1/movies", null);
                searchOn = 1;
                return;
            }

            // 검색어 입력시 검색어에 해당되는 영화를 movie_id 순으로 가져옴
            Log.i("Movie_app", "검색어 입력 : " + search);
            query = "?offset=" + offset + "&limit=" + limit + "&keyword=" + search;
            getRequest(Request.Method.GET, "/api/v1/movies/search", null);
            searchOn = 2;
        }

        // 버튼 클릭 : 연도로 정렬
        if (v == btn_by_year) {
            Log.i("Movie_app", "버튼 클릭 : 연도로 정렬");

            // 버튼 누를 때마다 리스트 초기화
            if (movieArrayList.size() > 0) {
                Log.i("Movie_app", "리스트 초기화");
                movieArrayList.clear();
            }

            // 검색어 입력, 미입력과 상관없이 가져온 데이터를 개봉 연도로 정렬함
            if (search.isEmpty()) {
                Log.i("Movie_app", "검색어 미입력");
            } else {
                Log.i("Movie_app", "검색어 입력 : " + search);
            }

            query = "?offset=" + offset + "&limit=" + limit + "&order=" + order + "&keyword=" + search;
            getRequest(Request.Method.GET, "/api/v1/movies/year", null);
            searchOn = 3;

            // order 값을 변경하기 전에, 추가 목록을 불러올 때 '변경된 order' 값을 불러오는 것을 막기 위한 셋팅
            // addOrder 값은 목록을 추가로 불러오는 곳(스크롤)에서만 사용함.
            addOrder = order;

            // 버튼을 누를 때마다 asc, desc 순서 변환
            if (order == 1) {
                order = 0;
            } else if (order == 0) {
                order = 1;
            }
        }

        // 버튼 클릭 : 관객수로 정렬
        if (v == btn_by_attnd) {
            Log.i("Movie_app", "버튼 클릭 : 관객수로 정렬");

            // 버튼 누를 때마다 리스트 초기화
            if (movieArrayList.size() > 0) {
                Log.i("Movie_app", "리스트 초기화");
                movieArrayList.clear();
            }

            // 검색어 입력, 미입력과 상관없이 가져온 데이터를 관객수로 정렬함
            if (search.isEmpty()) {
                Log.i("Movie_app", "검색어 미입력");
            } else {
                Log.i("Movie_app", "검색어 입력 : " + search);
            }

            query = "?offset=" + offset + "&limit=" + limit + "&order=" + order + "&keyword=" + search;
            getRequest(Request.Method.GET, "/api/v1/movies/attnd", null);
            searchOn = 4;

            // order 값을 변경하기 전에, 추가 목록을 불러올 때 '변경된 order' 값을 불러오는 것을 막기 위한 셋팅
            // addOrder 값은 목록을 추가로 불러오는 곳(스크롤)에서만 사용함.
            addOrder = order;

            // 버튼을 누를 때마다 asc, desc 순서 변환
            if (order == 1) {
                order = 0;
            } else if (order == 0) {
                order = 1;
            }
        }
    }

    // R.menu.menu_main.xml 파일을 엮어준다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 우측 상단 메뉴 버튼들을 누르면 무슨 액션을 할지 설정하는 곳
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 버튼 아이템 빼내는 코드
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        // 바로 아래는 action_settings 아이디를 가진 버튼의 액션 설정
        if (id == R.id.join_on) {
            Log.i("Movie_app", "버튼 클릭 : 회원 가입");
            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.login) {
            Log.i("Movie_app", "버튼 클릭 : 로그인");
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
            return true; // 버튼의 마지막에는 이걸 꼭 해줘야 함. 그래야 이 아래를 실행을 안 함.
        }

        if (id == R.id.favorite) {
            Log.i("Movie_app", "버튼 클릭 : 즐겨찾기");
            Intent i = new Intent(MainActivity.this, Favorite.class);
            startActivity(i);
            finish();
            return true;
        }

        if(id == R.id.logout) {
            Log.i("Movie_app", "버튼 클릭 : 로그아웃");
            // 토큰이 없으면 리턴
            Log.i("Movie_app", ""+token);
            if(token == null){
                Toast.makeText(MainActivity.this,"오류 : 로그인이 되어있지 않습니다",Toast.LENGTH_SHORT).show();
                return true;
            }else{
                query = "";
                logoutRequest(Request.Method.DELETE, "/api/v1/users/logout", null);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Request.Method.GET = 0, POST = 1, PUT = 2, DELETE = 3, HEAD = 4
    public void getRequest(int method, final String api_url, JSONObject object) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
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

                                movie = new Movie(id, title, genre, attendance, year);
                                movieArrayList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, movieArrayList);
                        recyclerView.setAdapter(recyclerViewAdapter);

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
        requestQueue.add(jsonObjectRequest);
    }

    public void addRequest(int method, final String api_url, JSONObject object) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
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

                                movie = new Movie(id, title, genre, attendance, year);
                                movieArrayList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
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
        requestQueue.add(jsonObjectRequest);
    }

    public void logoutRequest(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Movie_app", Utils.BASE_URL + api_url + query);

                        try {
                            boolean success = response.getBoolean("success");
                            if(success){
                                // 토큰 삭제
                                SharedPreferences preferences =
                                        getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token", null);

                                editor.apply();

                                Toast.makeText(MainActivity.this,"성공적으로 로그아웃되었습니다", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this,"로그아웃 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"로그아웃 실패", Toast.LENGTH_SHORT).show();
                        return;
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
        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(recyclerViewAdapter != null){
            recyclerViewAdapter.notifyDataSetChanged();
        }

    }
}