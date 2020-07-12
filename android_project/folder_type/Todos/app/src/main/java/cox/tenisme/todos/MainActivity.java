package cox.tenisme.todos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cox.tenisme.todos.adapter.RecyclerViewAdapter;
import cox.tenisme.todos.model.Todos;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    public static final String URL = "http://jsonplaceholder.typicode.com/todos";
    ArrayList<Todos> todoArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Todos", "Start onCreate");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("Todos", "onResponse");
                    for(int i = 0 ; i < response.length() ; i++){
                        try {
                            // 어레이에서 오브젝트 가져오기
                            JSONObject object = response.getJSONObject(i);
                            // 오브젝트에서 각 키값 파싱하기
                            int userId = object.getInt("userId");
                            int id = object.getInt("id");
                            String title = object.getString("title");
                            boolean completed = object.getBoolean("completed");
                            // 가져온 키값들을 Todos 객체 메모리에 "각각" 저장하기
                            Todos todos = new Todos(userId,id,title,completed);
                            // todoArrayList 에 생성된 Todos 객체들을 저장하기
                            todoArrayList.add(todos);
//                            // 잘 저장되었는지 확인용 로그 찍어보기
//                            Log.i("Todos",i+"번째 데이터 : "
//                                    +todoArrayList.get(i).getUserId()
//                                    +" / "+todoArrayList.get(i).getId()
//                                    +" / "+todoArrayList.get(i).getTitle()
//                                    +" / "+todoArrayList.get(i).isCompleted());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, todoArrayList);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Todos", "error : " + error.toString());
                }
            });

        requestQueue.add(jsonArrayRequest); // 아ㅏ아아아ㅏ아ㅏ아앙아악
    }
}