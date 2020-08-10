package com.tenisme.photosns;

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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.photosns.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    EditText edit_device;
    EditText edit_id;
    EditText edit_email;
    EditText edit_passwd;
    EditText edit_check_passwd;
    Button btn_join;
    Button btn_login;

    int device;
    String id;
    String email;
    String passwd;
    String checkPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인 되어있을 경우(토큰값이 있을 경우) 웰컴 액티비티로 스킵하는 코드
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if(token != null){
            Intent i = new Intent(MainActivity.this, Welcome.class);
            startActivity(i);
            finish();
            return;
        }

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        edit_device = findViewById(R.id.edit_device);
        edit_id = findViewById(R.id.edit_id);
        edit_email = findViewById(R.id.edit_email);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_check_passwd = findViewById(R.id.edit_check_passwd);
        btn_join = findViewById(R.id.btn_join);
        btn_login = findViewById(R.id.btn_login);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device = Integer.parseInt(edit_device.getText().toString().trim());
                id = edit_id.getText().toString().trim();
                email = edit_email.getText().toString().trim();
                passwd = edit_passwd.getText().toString().trim();
                checkPasswd = edit_check_passwd.getText().toString().trim();

                // 에러 처리 더 필요함

                if (id.length() > 20) {
                    Toast.makeText(MainActivity.this, "아이디는 20자 이내로 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.contains("@") == false) {
                    Toast.makeText(MainActivity.this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.length() > 100) {
                    Toast.makeText(MainActivity.this, "이메일은 100자 이내로 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() < 4 || passwd.length() > 12) {
                    Toast.makeText(MainActivity.this, "비밀번호는 4자리 이상 12자리 이하입니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.equalsIgnoreCase(checkPasswd) == false) {
                    Toast.makeText(MainActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

               JSONObject object = new JSONObject();
                try {
                    object.put("loginId", id);
                    object.put("email", email);
                    object.put("passwd", passwd);
                    object.put("device_id", device);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Log.i("Photo_sns", ""+object);

                // 서버로 이메일과 비밀번호를 전송한다.
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        Utils.BASE_URL + "/api/v1/photo_sns/user", object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Photo_sns", response.toString());

                                try {
                                    String token = response.getString("token");
                                    SharedPreferences sharedPreferences =
                                            getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("token", token);
                                    editor.apply();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent i = new Intent(MainActivity.this, Welcome.class);
                                startActivity(i);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Photo_sns", "Error : " + error.toString());
                            }
                        }
                );
                request.setRetryPolicy(new RetryPolicy() {
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
                requestQueue.add(request);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        });

    }
}