package com.tenisme.mymaps;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<MarkerOptions> list = new ArrayList<>(); // 여기에 마커 옵션을 담을 것임.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // onMapReady 함수(자동 생성 함수)를 콜백함
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 여러개의 마커를 처리하는 방법(ArrayList 활용) - 여러개의 마커를 처리할 땐 MarkerOptions 로 마커를 만든다.
        // 1. 마커 옵션을 만든다.
        MarkerOptions options1 = new MarkerOptions().position(new LatLng(37.541647, 126.840399)).title("5호선 화곡역");
        // 2. ArrayList 에 넣어준다.
        list.add(options1);

        MarkerOptions options2 = new MarkerOptions().position(new LatLng(37.540602, 126.837662)).title("화곡역 메가박스");
        list.add(options2);
        // 3. 위 ArrayList 를 지도에 표시한다
        for(MarkerOptions options : list){
            // 클래스 변수 : 어레이리스트_변수 -> 이 어레이리스트에 저장된 것들을 0부터 하나씩 뽑아서 변수에 저장한다. 변수의 속성인 클래스는 어레이리스트의 속성과 같아야 한다.
            // 뽑아낸 인스턴스를 for 문 안에서 다양하게 활용할 수 있다.
            mMap.addMarker(options);
        }
        // 지도의 중심으로 잡고 싶은 좌표를 넣어주면 지도의 중심으로 "설정"된다.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.540122, 126.839061), 18));

//        //37.541253, 126.838145 // 더조은강서
//        //37.540122, 126.839061 // 신월초
//        //37.541647, 126.840399 // 화곡역
//        //37.540602, 126.837662 // 화곡 메가박스
//        // Add a marker in Sydney and move the camera
//        // 1. GPS 정보 먼저 셋팅
//        LatLng sydney = new LatLng(37.541253, 126.838145);
//        LatLng school = new LatLng(37.540122,126.839061);
//        LatLng subway = new LatLng(37.541647,126.840399);
//        LatLng megaBox = new LatLng(37.540602,126.837662);
//        // 2. 구글맵에 마커 등록
//        mMap.addMarker(new MarkerOptions().position(sydney).title("더조은강서")
//        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
//            // 마커 아이콘 컬러 바꾸기
//        mMap.addMarker(new MarkerOptions().position(school).title("신월초등학교"));
//        mMap.addMarker(new MarkerOptions().position(subway).title("5호선 화곡역"));
//        mMap.addMarker(new MarkerOptions().position(megaBox).title("화곡역 메가박스"));
//        // 3. 지도 타입을 바꿔본다.
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        // 4. 화면의 중앙을 설정해서 지도를 보여주는 것.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(subway, 18));
    }
}