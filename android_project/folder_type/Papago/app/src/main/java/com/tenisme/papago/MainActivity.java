package com.tenisme.papago;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.papago.adapter.RecyclerViewAdapter;
import com.tenisme.papago.data.DatabaseHandler;
import com.tenisme.papago.model.Translations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    public static final String baseURL = "https://openapi.naver.com/v1/papago/n2mt";

    EditText editTrans;
    Button btnTrans;
    RadioGroup radioGroup;

    int checkedId;
    String before;
    String trans;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Translations> translationsArrayList;
    Translations new_translations;

    DatabaseHandler dh;

    JSONObject jsonObject;
    JSONObject messageObject;
    JSONObject resultObject;

//    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Papago","onCreate");

        editTrans = findViewById(R.id.editTrans);
        btnTrans = findViewById(R.id.btnTrans);
        radioGroup = findViewById(R.id.radioGroup);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        dh = new DatabaseHandler(MainActivity.this);
        translationsArrayList = dh.getAllTrans();

        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,translationsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedId = radioGroup.getCheckedRadioButtonId();
                before = editTrans.getText().toString().trim();
                if(before.isEmpty()){
                    Toast.makeText(MainActivity.this,"번역할 내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkedId == -1){
                    Toast.makeText(MainActivity.this,"번역할 언어를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                setRequestQueue();
            }
        });
    }

    public void setRequestQueue(){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Papago","response : "+response);
                        try {
                            jsonObject = new JSONObject(response);
                            messageObject = jsonObject.getJSONObject("message");
                            resultObject = messageObject.getJSONObject("result");
                            String result = resultObject.getString("translatedText");

                            if(checkedId==R.id.radioEng){
                                trans = "한국어 >> 영어";
                            }else if(checkedId==R.id.radioZhCn){
                                trans = "한국어 >> 중국어 간체";
                            }else if(checkedId==R.id.radioZhTw){
                                trans = "한국어 >> 중국어 번체";
                            }else if(checkedId==R.id.radioTh){
                                trans = "한국어 >> 태국어";
                            }

                            new_translations = new Translations(trans,before,result);
                            dh.addTrans(new_translations);
                            translationsArrayList = dh.getAllTrans();

                            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,translationsArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);

//                            mLayoutManager = new LinearLayoutManager(MainActivity.this);
//                            mLayoutManager.setReverseLayout(true);
//                            mLayoutManager.setStackFromEnd(true);
//                            recyclerView.setLayoutManager(mLayoutManager);
                            // DatabaseHandler 의 .getAllTrans()에서 order by ~ desc 로 역순 정렬하는 게 더 낫다고 해서 여기는 주석 처리함.

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Papago","error : "+error);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                params.put("X-Naver-Client-Id","Client-Id");
                params.put("X-Naver-Client-Secret","Client-Secret");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("source","ko");

                if(checkedId==R.id.radioEng){
                    params.put("target","en");
                }else if(checkedId==R.id.radioZhCn){
                    params.put("target","zh-CN");
                }else if(checkedId==R.id.radioZhTw){
                    params.put("target","zh-TW");
                }else if(checkedId==R.id.radioTh){
                    params.put("target","th");
                }

                params.put("text",before);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}