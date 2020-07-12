package com.tenisme.places;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tenisme.places.model.Store;

import java.util.ArrayList;

public class AllMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // 구글맵 처리 - 아래 방법은 이미 구글맵 API 가 등록되어 있다는 전제임.
    // 구글맵 처리 0
        // 0-1. gradle 에서 implementation 'com.google.android.gms:play-services-maps:17.0.0' 추가
        // 0-2. activity_maps.xml, google_maps_api.xml 복붙
        // 0-3. AndroidManifest.xml 에 아래 복붙
            // <meta-data
            // android:name="com.google.android.geo.API_KEY"
            // android:value="@string/google_maps_key" />
        // 0-4. 구글 APIs 사용자 인증 정보 - 등록된 구글맵 API 키에 이 앱의 패키지명과 google_maps_api.xml 의 SHA-1 certificate fingerprint 코드를 등록
    // 구글맵 처리1
    private GoogleMap mMap;

    ArrayList<Store> stores = new ArrayList<>();
    Intent i;
    LatLng nowHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        i = getIntent();
        double nowLat = i.getDoubleExtra("nowLat",37.541647);
        double nowLng = i.getDoubleExtra("nowLng",126.840399);
        nowHere = new LatLng(nowLat,nowLng);
        stores = (ArrayList<Store>) i.getSerializableExtra("storeList");
        Log.i("AAA","Stores size : "+stores.size());

        // 구글맵 처리2
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // onMapReady 함수(자동 생성 함수)를 콜백함
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(nowHere).title("Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowHere, 17));

        for(Store store : stores){
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(store.getLat(),store.getLng()))
                    .title(store.getName())
                    .snippet(store.getAddr());
            mMap.addMarker(options);
        }
    }
}