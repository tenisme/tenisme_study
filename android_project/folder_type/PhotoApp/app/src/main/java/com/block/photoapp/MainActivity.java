package com.block.photoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.block.photoapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edit_email;
    EditText edit_passwd1;
    EditText edit_passwd2;
    Button btn_register;
    Button btn_login;


    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if(token != null){
            Intent i = new Intent(MainActivity.this, Welcome.class);
            startActivity(i);
            finish();
            return;
        }

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        edit_email = findViewById(R.id.edit_email);
        edit_passwd1 = findViewById(R.id.edit_passwd1);
        edit_passwd2 = findViewById(R.id.edit_passwd2);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = edit_email.getText().toString().trim();
                final String passwd1 = edit_passwd1.getText().toString().trim();
                String passwd2 = edit_passwd2.getText().toString().trim();

                if(email.contains("@") == false){
                    Toast.makeText(MainActivity.this, "이메일 형식이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd1.length() < 4 || passwd1.length() > 12){
                    Toast.makeText(MainActivity.this, "비밀번호 길이는 4자리 이상,12자리 이하입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd1.equalsIgnoreCase(passwd2) == false){
                    Toast.makeText(MainActivity.this, "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("email", email);
                    object.put("passwd", passwd1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버로 이메일과 비밀번호를 전송한다.
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.BASE_URL + "/api/v1/users",
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("회원가입", response.toString());

                                try {
                                    String token = response.getString("token");
                                    SharedPreferences sharedPreferences =
                                            getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(MainActivity.this, Welcome.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("회원가입", error.toString());
                            }
                        }
                );
                requestQueue.add(request);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}