package cox.tenisme.employee;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cox.tenisme.employee.adapter.RecyclerViewAdapter;
import cox.tenisme.employee.model.Employee;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    // URL 상수 처리
    public static final String URL = "http://dummy.restapiexample.com/api/v1/employees";
    // Employee 타입을 담는, 비어있는 ArrayList 를 생성
    // ArrayList 는 미리 초기화를 해놓는다.
    ArrayList<Employee> employeeArrayList = new ArrayList<>();

    // RecyclerViewAdapter 클래스의 10번에 이어서
    // 11. RecyclerView 를 가져오고 셋팅하기 위한 선언
    RecyclerView recyclerView;
    // 12. RecyclerViewAdapter 를 사용하기 위해 RecyclerViewAdapter 선언
    RecyclerViewAdapter recyclerViewAdapter;
    // 13. 어댑터로 가져올 ArrayList 가 추가되어있는지 확인(이미 추가되어있음)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Employee", "Start onCreate");

        // 14. RecyclerView 셋팅을 위해 activity_main.xml 의 리사이클러뷰와 연결
        recyclerView = findViewById(R.id.recyclerView);
        // 14-1. 리사이클러뷰 기본 속성 2가지 셋팅
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        // 1. 발리의 리퀘스트큐 객체를 가져온다.
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        // 2. 제이슨리퀘스트 객체 생성 : 서버로부터 응답받았을 때 어떻게 처리할지를 코딩.
        JsonObjectRequest jsonObjectRequest =
                // 2-1. JsonObjectRequest 생성자에 들어가는 내용들(순서대로)
                    // http 프로토콜에서 get 메소드 설정,
                    // 요청할 URL 설정,
                    // 요청할 때 필요한 json 설정,
                    // 요청(서버)으로부터 정상적으로 응답받았을 때,
                    // 서버 에러 발생시
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
                            // 찾는 키값이 없을 경우 여기에서 처리한다
                            e.printStackTrace();
                        }
                        try {
                            // 3. JSONArray 가져오는 방법
                            JSONArray dataArray = response.getJSONArray("data");
                            Log.i("Employee", "key data : " + dataArray.toString());

                            // 4. for 루프로 JSONArray 에서 JSONObject 하나씩 전부 가져오기
                                // i는 0부터 불러오는 인덱스 번호가 된다.
                            for (int i = 0; i < dataArray.length(); i++) {
                                // 4-1. ★가져오려는 게 JSONObject 이므로 .get(인덱스)이 아니라 .getJSONObject(인덱스)로 가져와야 한다.
                                    // 가져오는{ } 범위가 어디서부터 어디까지인지를 실제로 보면서 잘 파악해야 한다.
                                JSONObject object = dataArray.getJSONObject(i);
                                    // Log.i("Employee","루프 "+i+"번째 : "+object.toString());
                                // 4-2. ★이렇게 불러낸 JSONObject 에서 키값으로 밸류 뽑아오기
                                String name = object.getString("employee_name");
                                    // Log.i("Employee","루프 "+i+"번째 사람의 이름은 "+name);
                                int id = object.getInt("id");
                                int salary = object.getInt("employee_salary");
                                int age = object.getInt("employee_age");
                                    // Log.i("Employee","루프 "+i+"번째 사람의 아이디는 "+id);
                                    // Log.i("Employee","루프 "+i+"번째 사람의 연봉은 "+salary);
                                    // Log.i("Employee","루프 "+i+"번째 사람의 나이는 "+salary);
                                // 4-3. 파싱한 데이터를, 데이터를 저장하기 위해 만들어놓은 클래스의 객체로(메모리에) 저장한다.
                                    // 각 아이디마다 하나의 새 객체로 만들어 저장해야 한다는 걸 걸 잊지 말자.
                                Employee employee = new Employee(id, name, salary, age);
                                // 4-4. 이렇게 저장한 Employee 타입을 Employee 타입을 담는 ArrayList 에 저장한다.
                                employeeArrayList.add(employee);
                                // 5-0. activity_main.xml 에 RecyclerView 를 배치하고,
                                    // 5-1. "어댑터"에 연결할 "리스트 양식" 레이아웃 파일(~_row.xml) 파일을 만들고,
                                    // 5-2. RecyclerViewAdapter.java 파일까지 설계한 다음 RecyclerView 를 최종적으로 셋팅하기 위해 다시 이곳으로 돌아온다.
                            }
                            // 15. RecyclerViewAdapter 인스턴스 생성 : ArrayList 에 저장한 데이터들을 어댑터와 연결
                            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, employeeArrayList);
                            // 16. RecyclerView 에 설정한 어댑터(recyclerViewAdapter)를 배치
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
        // jsonObjectRequest 를 호출!!!!!!!!!!1 호출 필수!!!!!!!!!!!!!!!!!!!1
        requestQueue.add(jsonObjectRequest);

        // 주의 : 위 코드 아래에 어댑터 연결하는 코드 넣으면 절대 안됨.
            // 위 코드까지 실행되면, 네트워크 갔다와서, 어레이리스트에 네트워크가 쌓여있는 상태가 "아니다"
            // 이 아래는 requestQueue 가 실행된 다음(실행 "다 끝난" 다음) 아래로 내려온 상태인 것임.
            // Array 에 저장한 데이터를 처리하려면(저장된 데이터들을 어댑터에 추가해서 처리하려면) 이 아래가 아니라,
            // 서버에서 데이터를 정상적으로 받아온 상태에서 실행되는 Response.Listener<JSONObject>(){} 안에서,
            // for 루프가 끝난 다음에(★ArrayList 에 데이터를 모두 저장한 다음에★) 어댑터를 처리해야 한다.
    }
}