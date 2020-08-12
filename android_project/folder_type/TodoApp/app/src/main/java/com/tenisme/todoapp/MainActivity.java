package com.tenisme.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.todoapp.adapter.RecyclerViewAdapter;
import com.tenisme.todoapp.model.Todo;
import com.tenisme.todoapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    Todo todo;
    ArrayList<Todo> todoArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    int endSearch = 0;

    int offset = 0;
    int limit = 25;
    String api_url = "";
    String query = "";

    JSONArray itemArray;
    JSONObject object;

    boolean success = false;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 리스트의 끝에 도달하면 아래를 실행하지 않고 리턴
                if (endSearch == 1) {
                    return;
                }

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if (lastPosition + 1 == totalCount) {
                    query = "?offset=" + offset + "&limit=" + limit;
                    api_url = Utils.BASE_URL + "/api/v1/todo" + query;
                    addRequest(Request.Method.GET, api_url, null);
                }
            }
        });

        query = "?offset=" + offset + "&limit=" + limit;
        api_url = Utils.BASE_URL + "/api/v1/todo" + query;
        getRequest(Request.Method.GET, api_url, null);
    }

    public void getRequest(int method, String api_url, JSONObject object) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(method, api_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            success = response.getBoolean("success");
                            cnt = response.getInt("cnt");

                            itemArray = response.getJSONArray("items");

                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject objectInItems = itemArray.getJSONObject(i);
                                int id = objectInItems.getInt("id");
                                String title = objectInItems.getString("title");
                                String date = objectInItems.getString("date");
                                int completed = itemArray.getJSONObject(i).getInt("completed");

                                todo = new Todo(id, title, date, completed);
                                todoArrayList.add(todo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, todoArrayList);
                        recyclerView.setAdapter(recyclerViewAdapter);

                        offset = offset + cnt;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Todo_app", "ERROR : " + error.toString());
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

    public void addRequest(int method, String api_url, JSONObject object) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(method, api_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            success = response.getBoolean("success");
                            cnt = response.getInt("cnt");

                            // cnt 가 0이면(리스트의 끝까지 왔으면) endSearch 를 ON 하고 리턴
                            if (cnt == 0) {
                                endSearch = 1;
                                return;
                            }

                            itemArray = response.getJSONArray("items");

                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject objectInItems = itemArray.getJSONObject(i);
                                int id = objectInItems.getInt("id");
                                String title = objectInItems.getString("title");
                                String date = objectInItems.getString("date");
                                int completed = itemArray.getJSONObject(i).getInt("completed");

                                todo = new Todo(id, title, date, completed);
                                todoArrayList.add(todo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerViewAdapter.notifyDataSetChanged();

                        offset = offset + cnt;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Todo_app", "ERROR : " + error.toString());
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

    public void checkTodoRequest(int position) {

        todo = todoArrayList.get(position);
        int todo_id = todo.getTodo_id();

        object = new JSONObject();
        try {
            object.put("todo_id", todo_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api_url = Utils.BASE_URL + "/api/v1/todo/check";

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, api_url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        todo.setCompleted(1);
                        recyclerViewAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Todo 체크!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Todo_app", "ERROR : " + error.toString());
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

    public void uncheckTodoRequest(int position) {

        todo = todoArrayList.get(position);
        int todo_id = todo.getTodo_id();

        object = new JSONObject();
        try {
            object.put("todo_id", todo_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api_url = Utils.BASE_URL + "/api/v1/todo/uncheck";

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, api_url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        todo.setCompleted(0);
                        recyclerViewAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Todo 체크 취소", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Todo_app", "ERROR : " + error.toString());
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
}