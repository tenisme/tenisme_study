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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.block.photoapp.adapter.RecyclerViewAdapter;
import com.block.photoapp.api.NetworkClient;
import com.block.photoapp.api.PostApi;
import com.block.photoapp.api.UserApi;
import com.block.photoapp.model.Item;
import com.block.photoapp.model.Post;
import com.block.photoapp.model.PostRes;
import com.block.photoapp.model.UserReq;
import com.block.photoapp.model.UserRes;
import com.block.photoapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Welcome extends AppCompatActivity {

    Button btn_logout;
    Button btnPosting;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<Item> postArrayList = new ArrayList<>();

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_logout = findViewById(R.id.btn_logout);
        btnPosting = findViewById(R.id.btnPosting);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Welcome.this));

        btn_logout.setOnClickListener(view -> {
            // 1. 쉐어드 프리퍼런스에 저장되어있는 토큰을 가져온다.
            SharedPreferences sharedPreferences =
                    getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
            final String token = sharedPreferences.getString("token", null);
            Log.i("AAA", token);

            Retrofit retrofit = NetworkClient.getRetrofitClient(Welcome.this);
            UserApi userApi = retrofit.create(UserApi.class);
            Call<UserRes> call = userApi.logoutUser("Bearer "+token);

            call.enqueue(new Callback<UserRes>() {
                @Override
                public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                    if(response.isSuccessful()){
                        if(response.body().isSuccess()){
                            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token", null);
                            editor.apply();

                            Intent i = new Intent(Welcome.this, Login.class);
                            startActivity(i);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRes> call, Throwable t) {

                }
            });

        });

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        getNetworkData();

        btnPosting.setOnClickListener(view -> {
            Intent i = new Intent(Welcome.this, Posting.class);
            startActivity(i);
        });


    }

    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(Welcome.this);
        PostApi postsApi = retrofit.create(PostApi.class);
        // ResponseBody 는 gson 을 클래스에 매핑시켜서 자동으로 파싱해야 한다. ?????

        Call<PostRes> call = postsApi.getPosts("Bearer "+token, 0, 25);

        call.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                // response.body() 는 PostRes 클래스이다.
                // PostRes.get(0)는 response.body().get(0)과 같음
                // PostRes.get(0) => List<Item> 의 첫 번째 Item 객체
                // PostRes.get(0).getContent() => 위의 Item 객체에 저장된 content 값
                Log.i("AAA", response.body().getSuccess().toString());
                Log.i("AAA", response.body().getItems().get(0).getContent());
                Log.i("AAA", response.body().getCnt().toString());

                postArrayList = response.body().getItems(); // 빨간줄 뜨면 체인지 필드 어레이리스트 선택
                adapter = new RecyclerViewAdapter(Welcome.this, postArrayList); // 빨간줄 뜨면 알트 엔터 하고 첫번째꺼 엔터
                // 리사이클러뷰 어댑터 이동해서 어레이 리스트 부분을 List<Item>으로 전부 바꿔준다
                // 빨간 거 뜨는 Post 부분을 알트 엔터 > 첫번째거 엔터 아무튼 빨간 거 뜨는 거는 전부 변경
                recyclerView.setAdapter(adapter);
                // 이대로 실행했을 때 나오면 됨
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
                Log.i("AAA", t.toString());
            }
        });
    }


}