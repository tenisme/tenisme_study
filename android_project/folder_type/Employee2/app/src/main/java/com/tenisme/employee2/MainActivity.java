package com.tenisme.employee2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.employee2.adapter.RecyclerViewAdapter;
import com.tenisme.employee2.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    public static final String URL = "http://dummy.restapiexample.com/api/v1/employees";

    ArrayList<Employee> employeeArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.GET,
                        URL,
                        null,
                        new Response.Listener<JSONObject>() {
                            // 네트워크를 통해서 제이슨을 받아서 처리하는 형식
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Employee", "result : " + response.toString());
                                try {
                                    // 찾는 키값이 있으면 여기서 처리해준다
                                    String value = response.getString("status");
                                    Log.i("Employee", "status : " + value);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONArray dataArray = response.getJSONArray("data");
                                    Log.i("Employee", "key data : " + dataArray.toString());

                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject object = dataArray.getJSONObject(i);

                                        String name = object.getString("employee_name");
                                        int id = object.getInt("id");
                                        int salary = object.getInt("employee_salary");
                                        int age = object.getInt("employee_age");

                                        Employee employee = new Employee(id, name, salary, age);
                                        employeeArrayList.add(employee);
                                    }
                                    recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, employeeArrayList);

                                    recyclerView.setAdapter(recyclerViewAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Employee", "error : " + error.toString());

                            }
                        });
        requestQueue.add(jsonObjectRequest);
    }
}
