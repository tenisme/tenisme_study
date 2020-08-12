package com.block.photoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.block.photoapp.adapter.RecyclerViewAdapter;
import com.block.photoapp.api.NetworkClient;
import com.block.photoapp.api.PostsApi;
import com.block.photoapp.model.Post;
import com.block.photoapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Welcome extends AppCompatActivity {

    Button btn_logout;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<Post> postArrayList = new ArrayList<>();

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_logout = findViewById(R.id.btn_logout);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Welcome.this));

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 쉐어드 프리퍼런스에 저장되어있는 토큰을 가져온다.
                SharedPreferences sharedPreferences =
                        getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                final String token = sharedPreferences.getString("token", null);
                Log.i("AAA", token);

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.DELETE,
                        Utils.BASE_URL + "/api/v1/users/logout",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA", "logout : " + response.toString());
                                try {
                                    boolean success = response.getBoolean("success");
                                    if(success == true){
                                        // 토큰을 지워줘야 한다.
                                        SharedPreferences sharedPreferences =
                                                getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("token", null);
                                        editor.apply();

                                        Intent i = new Intent(Welcome.this, Login.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        // 토스트 띄운다.
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // 토스트 띄운다. 로그아웃 실패라고 토스트.
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

                Volley.newRequestQueue(Welcome.this).add(request);

            }
        });


        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        Log.i("AAA", "token : "+token);

        getNetworkData();
    }

    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(Welcome.this);

        PostsApi postsApi = retrofit.create(PostsApi.class);

        Call<ResponseBody> call = postsApi.getPosts(token, 0, 25);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.i("AAA", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("AAA", t.toString());
            }
        });

    }


}