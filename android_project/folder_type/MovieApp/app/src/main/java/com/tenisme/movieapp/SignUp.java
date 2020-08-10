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

public class SignUp extends AppCompatActivity {

    EditText edit_id;
    EditText edit_email;
    EditText edit_passwd;
    EditText edit_check_passwd;
    Button btn_join;

    String id;
    String email;
    String passwd;
    String checkPasswd;

    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if(token != null){
            Intent i = new Intent(SignUp.this, Welcome.class);
            startActivity(i);
            finish();
            return;
        }

        edit_id = findViewById(R.id.edit_id);
        edit_email = findViewById(R.id.edit_email);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_check_passwd = findViewById(R.id.edit_check_passwd);
        btn_join = findViewById(R.id.btn_join);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = edit_id.getText().toString().trim();
                email = edit_email.getText().toString().trim();
                passwd = edit_passwd.getText().toString().trim();
                checkPasswd = edit_check_passwd.getText().toString().trim();

                if (id.length() > 20) {
                    Toast.makeText(SignUp.this, "아이디는 20자 이내로 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.contains("@") == false) {
                    Toast.makeText(SignUp.this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.length() > 100) {
                    Toast.makeText(SignUp.this, "이메일은 100자 이내로 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() < 4 || passwd.length() > 12) {
                    Toast.makeText(SignUp.this, "비밀번호는 4자리 이상 12자리 이하입니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.equalsIgnoreCase(checkPasswd) == false) {
                    Toast.makeText(SignUp.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("login_id", id);
                    object.put("email", email);
                    object.put("passwd", passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                signUpRequest(Request.Method.POST,"/api/v1/users",object);

            }
        });
    }

    public void signUpRequest(int method, final String api_url, JSONObject object) {
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

                        Intent i = new Intent(SignUp.this, Welcome.class);
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
        Volley.newRequestQueue(SignUp.this).add(jsonObjectRequest);
    }
}