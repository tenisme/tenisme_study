package com.tenisme.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.movieapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText edit_id;
    EditText edit_passwd;
    Button btn_login;

    String id;
    String passwd;

    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if(token != null){
            Intent i = new Intent(Login.this, Welcome.class);
            startActivity(i);
            finish();
            return;
        }

        edit_id = findViewById(R.id.edit_id);
        edit_passwd = findViewById(R.id.edit_passwd);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = edit_id.getText().toString().trim();
                passwd = edit_passwd.getText().toString().trim();

                if(passwd.isEmpty() || passwd.length() < 4 || passwd.length() > 12){
                    Toast.makeText(Login.this,"패스워드는 4자 이상 12자 이하로 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("id_or_email", id);
                    object.put("passwd", passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loginRequest(Request.Method.POST, "/api/v1/users/login", object);
            }
        });
    }

    public void loginRequest(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Movie_app", Utils.BASE_URL + api_url + query);

                        try {
                            boolean success = response.getBoolean("success");
                            String token = response.getString("token");
                            String login_id = response.getString("login_id");

                            Log.i("Movie_app", "success : " + success + ", login_id : " + login_id);

                            // 서버에서 준(res) 토큰을 SharedPreferences 에 저장
                            SharedPreferences sharedPreferences =
                                    getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", token);
                            editor.putString("login_id", login_id);

                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(Login.this, Welcome.class);
                        startActivity(i);
                        finish();
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
        Volley.newRequestQueue(Login.this).add(jsonObjectRequest);
    }
}