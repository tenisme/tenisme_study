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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.photosns.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText edit_device;
    EditText edit_id;
    EditText edit_passwd;
    Button btn_login;
    Button btn_join;

    int device;
    String id;
    String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_device = findViewById(R.id.edit_device);
        edit_id = findViewById(R.id.edit_id);
        edit_passwd = findViewById(R.id.edit_passwd);
        btn_login = findViewById(R.id.btn_login);
        btn_join = findViewById(R.id.btn_join);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device = Integer.parseInt(edit_device.getText().toString().trim());
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
                    object.put("device_id", device);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Log.i("Photo_sns", ""+object);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        Utils.BASE_URL+"/api/v1/photo_sns/user/login", object,
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

                                Intent i = new Intent(Login.this, Welcome.class);
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
                Volley.newRequestQueue(Login.this).add(request);
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}