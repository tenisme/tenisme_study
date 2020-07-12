package com.tenisme.places;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.places.adapter.RecyclerViewAdapter;
import com.tenisme.places.model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?language=ko&radius=20000";
    private final String KEY = "&key=AIzaSyBVQoPwiWgyMt498eq2ObuNhL4KUBNcPls";
    private String url;

    EditText editSearch;
    Button btnSearch;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Store> stores = new ArrayList<>();
        // 어레이리스트 통째로 옮기기 - 선행 : serializable
    Store store;

    // GPS 좌표 가져오기 위한 멤버변수 선언.
    LocationManager locationManager;
    // 위치가 변경될 때마다 처리해줄 리스너를 멤버변수로 선언.
    LocationListener locationListener;

//    좌표는 이렇게 멤버변수로 빼도 됨. 아니면 함수 만들 때 필요한 곳에 파라미터로 넘기든지 하면 됨.
    double lat;
    double lng;

    String nextPageToken = ""; // 이거 없으면 널포인트익셉션 에러남.
    String pageToken = "";

    String name;
    String addr;
    double storeLat;
    double storeLng;
    String keyword = "";

    boolean isFirst = true;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = editSearch.getText().toString().trim();
                if(stores.size() > 0){
                    stores.clear();
                }
                getNetworkData();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        // recyclerView 의 Scroll 설정
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition+1 == totalCount){
                    // 아이템 추가! 입맛에 맞게 설정하시면됩니다.
                    // 여기서 네트워크를 이용해 다음 페이지들을 호출한다.
                    // nextPageToken, pageToken 2개를 멤버 변수로 셋팅.
                    if(!nextPageToken.isEmpty() && !nextPageToken.equals(pageToken)){
                        // 구글이 페이지를 줬는데 마지막 값이 없을 때 nextPageToken 은 비어있다.
                        // 넥스트토큰이 값이 있고, 넥스트토큰이 페이지토큰과 같지 않을 때 이 if를 실행한다.
                            // 값이 같으면 비어있는 곳에 호출하겠다는 이야기이므로 ㄴㄴ함.
                        pageToken = nextPageToken;
                        // 네트워크 호출. 기존 getJSON 을 호출하면 화면이 갱신되면서 화면이 맨 위로 가게 되므로, 네트워크를 가져오는 새로운 함수를 만든다.
                        addNetworkData();
                        // 데이터를 더이상 못 가져오는 것은 리스트가 거기까지이기 때문이다.
                    }
                }

            }
        });

        // 위치기반 서비스를 사용하기 위해서 안드로이드 시스템에 위치기반 서비스 요청.
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("AAA", location.toString());
                lat = location.getLatitude();
                lng = location.getLongitude();

                // 네트워크로 구글 플레이스 API 호출(한번만 실행하게 하는 방법을 사용 : isFirst 사용)
                    // 여기서 isFirst 는 플랙이라고 함. 플래그? ?-?
                if(isFirst){
                    isFirst = false; // 이렇게 설정하면 한 번만 호출하게 됨.
                    getNetworkData();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

            return;
        }
        // 로케이션매니저에 리스너 달아주기
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }

    private void addNetworkData() {

        String url = "";
        // url 셋팅
        if(keyword.isEmpty()){
            url = BASE_URL+KEY+"&location="+lat+","+lng+"&pagetoken="+pageToken;
        }else{
            url = BASE_URL+KEY+"&location="+lat+","+lng+"&keyword="+keyword+"&pagetoken="+pageToken;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.i("AAA","response : "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.isNull("next_page_token")) {
                                nextPageToken = jsonObject.getString("next_page_token");
                            }
                            JSONArray results = jsonObject.getJSONArray("results");
                            for(int i = 0 ; i < results.length() ; i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject geometry = item.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");
                                storeLat = location.getDouble("lat");
                                storeLng = location.getDouble("lng");
                                name = item.getString("name");
                                addr = item.getString("vicinity");

                                Store store = new Store(name,addr,storeLat,storeLng);
                                stores.add(store);
                            }
                            // 아래와 같이 셋팅해야 화면이 다시 위로 올라가지 않는다!
                            recyclerViewAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AAA","error : "+error.toString());

                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    private void getNetworkData() {

        String url = ""; // 널포인트익셉션을 피하기 위해 객체("")를 만들어준다. isEmpty() 는 null 값이 들어가면 공백으로 처리하지 않고 오류가 나서 꺼진다.
            // String url; 로만 선언하면, null 값이 기본 값이기 때문에 널포인트익셉션 오류가 날 수 있다.
                // String 은 객체라서 초기화하지 않으면 null 로 처리된다. int 등은 초기화하지 않아도 기본 값이 0이다.
        // url 셋팅
        if(keyword.isEmpty()){
            url = BASE_URL+KEY+"&location="+lat+","+lng;
        }else{
            url = BASE_URL+KEY+"&location="+lat+","+lng+"&keyword="+keyword;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.i("AAA","response : "+response);
                    try {
                        // StringRequest 로 가져올 땐 처음에 이런 방식으로 가져와야 함.
                        JSONObject jsonObject = new JSONObject(response);
                        // next_page_token 이 null 이 아니면 nextPageToken 에 이 값을 저장한다.
                        // 없으면 에러나서 꺼지므로 if는 꼭 지정해주자.
                        if(!jsonObject.isNull("next_page_token")) {
                            // 넥스트페이지 토큰값 뽑아오기
                            // 새로운 정보를 주는 키값. 좀이따 URL 에 붙여서 보내야 함.
                            nextPageToken = jsonObject.getString("next_page_token");
                            // 나중에 여기 말고 다른 데서 써야할 것들만 위와 같이 멤버 변수로 선언하자.
                        }
                        JSONArray results = jsonObject.getJSONArray("results");
                        for(int i = 0 ; i < results.length() ; i++){
                            JSONObject item = results.getJSONObject(i);
                            JSONObject geometry = item.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            // 위치의 좌표 뽑아내기
                            storeLat = location.getDouble("lat");
                            storeLng = location.getDouble("lng");
                            // 위치의 이름 뽑아내기
                            name = item.getString("name");
                            // 위치의 주소 뽑아내기
                            addr = item.getString("vicinity");

                            store = new Store(name,addr,storeLat,storeLng);
                            stores.add(store);
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, stores);
                        recyclerView.setAdapter(recyclerViewAdapter);

                    } catch (JSONException e) {
                        // 익셉션 Exception : 실행 도중에 문제가 발생할 경우 catch 에서 처리할 수 있도록 하는 코드
                        e.printStackTrace();

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("AAA","error : "+error.toString());

                }
            }
        );
        // ErrorCode - NullPointerException : 초기화한 변수가 없을 때 뜨는 오류. 여기서는 requestQueue 임.
        requestQueue.add(stringRequest);
    }

    // GPS 사용 관련 설정2
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
            // 위 코드는 onCreate 에 한 번만 써주면(한번만 실행되면) 된다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
                    locationListener); // 다시 호출할 때는 이것만 불러오면 됨.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 우측 상단 메뉴 버튼들을 누르면 무슨 액션을 할지 설정하는 곳
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 버튼 아이템 빼내는 코드
        int id = item.getItemId();

        // 바로 아래는 action_settings 아이디를 가진 버튼의 액션 설정
        if (id == R.id.menu_see_all_places) {
            i = new Intent(MainActivity.this,AllMapsActivity.class);
            i.putExtra("nowLat",lat);
            i.putExtra("nowLng",lng);
            i.putExtra("storeList",stores);
                // ★ arrayList intent 로 보내기 : serializable 선행, stores 에는 "현재" 리사이클러뷰에 나타난 모든 리스트가 저장되어 있다는 것을 잊지 말자. 이 리스트 그냥 보내기만 하면 된다.
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}