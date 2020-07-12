package com.tenisme.searchplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tenisme.searchplace.adapter.RecyclerViewAdapter;
import com.tenisme.searchplace.adapter.RecyclerViewAdapter_SavePlace;
import com.tenisme.searchplace.data.DatabaseHandler;
import com.tenisme.searchplace.model.SearchPlace;
import com.tenisme.searchstore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

        // todo : 저장해놓은 장소는 어댑터의 "장소 저장"을 "장소 저장됨"으로 바꾸고, 다시 클릭시 "저장을 취소하시겠습니까?"라는 알러트 다이얼로그를 띄운다.
            // "저장 취소"를 누르면 저장된 DB 에서 삭제되고, "아니오"를 누르면 아무 행동도 하지 않는다.
            // 저장되어있는 DB가 재검색 등으로 다시 보일 경우에도 우측 상단에 "장소 저장됨"이 보일 수 있도록 한다.

    RequestQueue requestQueue;
    private String baseURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
            "language=ko";
    private String KEY = "&key=AIzaSyBVQoPwiWgyMt498eq2ObuNhL4KUBNcPls";
    private String url;

    private LocationManager locationManager;
    private LocationListener locationListener;

    JSONArray resultsArray;
    JSONObject resultsObject;
    JSONObject geometryObject;
    JSONObject locationObject;

    ArrayList<SearchPlace> searchPlaces = new ArrayList<>(); // 검색 표시용
    ArrayList<SearchPlace> savedSearchPlaces = new ArrayList<>();
    SearchPlace searchPlace;
    RecyclerView recyclerView; // 검색 표시용
    RecyclerViewAdapter recyclerViewAdapter; // 검색 표시용

    Spinner spinnerPlaceType;
    ArrayAdapter arrayAdapter; // spinner 셋팅용

    EditText editDistance;
    EditText editSearchStore;
    Button btnSearch;

    String distance; // editDistance
    String search; // editSearchStore
    String placeType; // spinnerPlaceType

    Intent i;

    String nextPageToken;
    String pageToken = "";

    String searhUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("SearchStore","onCreate");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        
        spinnerPlaceType = findViewById(R.id.spinnerPlaceType);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceType.setAdapter(arrayAdapter);
        spinnerPlaceType.setOnItemSelectedListener(this);

        editDistance = findViewById(R.id.editDistance);
        editSearchStore = findViewById(R.id.editSearchStore);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);

    }

    public void getJSON(String url){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i("SearchStore","response : "+response.toString());
                        try {
                            resultsArray = response.getJSONArray("results");
//                            Log.i("SearchStore",resultsArray.toString());
                            for(int i = 0;i<resultsArray.length();i++) {
                                resultsObject = resultsArray.getJSONObject(i);
                                geometryObject = resultsObject.getJSONObject("geometry");
                                locationObject = geometryObject.getJSONObject("location");

                                double locationLat = locationObject.getDouble("lat"); // 위도
                                double locationLng = locationObject.getDouble("lng"); // 경도
                                String placeName = resultsObject.getString("name").trim(); // 장소 이름
                                String vicinity = resultsObject.getString("vicinity").trim(); // 장소의 주소
                                Log.i("SearchStore","가져온 거 : "+locationLat+", "+locationLng+", "+placeName+", "+vicinity);

                                searchPlace = new SearchPlace(locationLat,locationLng,placeName,vicinity);
                                searchPlaces.add(searchPlace);

                            }
                            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,searchPlaces);
                            recyclerView.setAdapter(recyclerViewAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("SearchStore","error : "+error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    // GPS 사용 관련 설정1 - 호출용
    public void callLocate(){
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

//                Log.i("SearchStore", location.toString());
                double lat = location.getLatitude(); // 지도에서 위도 정보 뽑아오기
                double lng = location.getLongitude(); // 지도에서 경도 정보 뽑아오기
                String latLng = "&location="+lat+","+lng;

                String radius = "&radius="+distance;

                search = editSearchStore.getText().toString().trim();

                if(search.isEmpty()){
                    url = baseURL+radius+latLng+KEY+placeType; // 호출할 url 설정
                    Log.i("SearchStore",url);
                }else{
                    String keyword = "&keyword="+search;
                    url = baseURL+radius+latLng+KEY+placeType+keyword; // 호출할 url 설정
                    Log.i("SearchStore",url);
                }

                getJSON(url);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // GPS 의 상태가 바뀌었을 때(아래의 두개와 같이 실행된다).
            }

            @Override
            public void onProviderEnabled(String provider) {
                // GPS 가 켜졌을 때 호출되는 함수
            }

            @Override
            public void onProviderDisabled(String provider) {
                // GPS 를 껐을 때 호출되는 함수
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
                locationListener);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
                    locationListener);
        }
    }

    // Spinner 항목 클릭시 설정
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position){
            case 0 :
                Log.i("SearchStore",Integer.toString(position));
                placeType = "";
                break;
            case 1 :
                Log.i("SearchStore",Integer.toString(position));
                placeType = "&type=bank";
                break;
            case 2 :
                Log.i("SearchStore",Integer.toString(position));
                placeType = "&type=atm";
                break;
            case 3 :
                Log.i("SearchStore",Integer.toString(position));
                placeType = "&type=library";
                break;
            case 4 :
                Log.i("SearchStore",Integer.toString(position));
                placeType = "&type=cafe";
                break;
            case 5 :
                Log.i("SearchStore",Integer.toString(position));
                placeType = "&type=restaurant";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Button 클릭시 설정
    @Override
    public void onClick(View v) {
        if(v == btnSearch){
            distance = editDistance.getText().toString().trim();
            if(distance.isEmpty()){
                Toast.makeText(MainActivity.this,"거리 입력은 필수입니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            // 검색 리스트를 초기화시킬 필요가 있을 때 설정
            if(searchPlaces.size() > 0){
                searchPlaces.clear();
            }
            callLocate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 액션바 버튼 셋팅
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 버튼 아이템 빼내는 코드
        int id = item.getItemId();

        // 바로 아래는 버튼의 액션 설정
        if (id == R.id.menu_favorite) {
            i = new Intent(MainActivity.this, SavePlace.class);
            startActivityForResult(i,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    // result Code 설정
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==0&&resultCode==RESULT_OK){
//            int saved = data.getIntExtra("saved",-1);
//        }
//    }
}