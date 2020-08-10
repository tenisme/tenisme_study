package com.tenisme.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.movieapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    Button btn_favorite;
    Button btn_logout;

    String token;

    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_favorite = findViewById(R.id.btn_favorite);
        btn_logout = findViewById(R.id.btn_logout);

        // 기기 로그아웃을 위해 저장된 토큰 가져오기
        SharedPreferences sharedPreferences =
                getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        btn_favorite.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 버튼 클릭 : 즐겨찾기로 이동
        if(v == btn_favorite){
            Log.i("Movie_app","버튼 클릭 : 즐겨찾기로 이동");
            Intent i = new Intent(Welcome.this, Favorite.class);
            startActivity(i);
            finish();
        }

        // 버튼 클릭 : 로그아웃
        if(v == btn_logout){
            Log.i("Movie_app","버튼 클릭 : 로그아웃");
            logoutRequest(Request.Method.DELETE, "/api/v1/users/logout", null);
        }
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

                                Toast.makeText(Welcome.this,"성공적으로 로그아웃되었습니다", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(Welcome.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(Welcome.this,"로그아웃 실패", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Welcome.this,"로그아웃 실패", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(Welcome.this).add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(Welcome.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}