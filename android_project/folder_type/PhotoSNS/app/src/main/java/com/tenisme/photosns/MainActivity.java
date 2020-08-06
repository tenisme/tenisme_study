package com.tenisme.photosns;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String baseUrl = "http://tenisme.ap-northeast-2.elasticbeanstalk.com";
    RequestQueue requestQueue;

    EditText edit_device;
    EditText edit_id;
    EditText edit_email;
    EditText edit_passwd;
    EditText edit_check_passwd;
    Button btn_join;

    String device;
    String id;
    String email;
    String passwd;
    String checkPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        edit_device = findViewById(R.id.edit_device);
        edit_id = findViewById(R.id.edit_id);
        edit_email = findViewById(R.id.edit_email);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_check_passwd = findViewById(R.id.edit_check_passwd);
        btn_join = findViewById(R.id.btn_join);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device = edit_device.getText().toString().trim();
                id = edit_id.getText().toString().trim();
                email = edit_email.getText().toString().trim();
                passwd = edit_passwd.getText().toString().trim();
                checkPasswd = edit_check_passwd.getText().toString().trim();

                // 에러 처리 더 필요함

                if(id.length() > 20){
                    Toast.makeText(MainActivity.this, "아이디는 20자 이내로 적어주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.contains("@") == false){
                    Toast.makeText(MainActivity.this, "이메일 형식이 올바르지 않습니다",Toast.LENGTH_SHORT).show();
                    return;
                }else if(email.length() > 100){
                    Toast.makeText(MainActivity.this, "이메일은 100자 이내로 적어주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwd.length() < 4 || passwd.length() > 12){
                    Toast.makeText(MainActivity.this, "비밀번호는 4자리 이상 12자리 이하입니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwd.equalsIgnoreCase(checkPasswd) == false){
                    Toast.makeText(MainActivity.this, "비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("device_id", device);
                    object.put("loginId", id);
                    object.put("email", email);
                    object.put("passwd", passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버로 이메일과 비밀번호를 전송한다.
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl+"/api/v1/photo_sns/user", object,
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Photo_sns",response.toString());
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Photo_sns","Error : "+error.toString());
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        return params;
                    }
                };
                requestQueue.add(request);
            }
        });

    }
}