package com.tenisme.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class MainActivity extends AppCompatActivity {

    // 선행 : 매니페스토 가서 인터넷 권한/GPS 권한 줘야 함.

    private String baseURL = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?w=768&h=1024&level=18&scale=2&format=png";

    private String clientID = "ivwktksgpi";
    private String clientSecret = "QW3IRJLjkE1trszxs2lmcBMcVuNexpITr9hCx4Nl";

    private LocationManager locationManager;
    private LocationListener locationListener;

    TextView txtGps;
    ImageView imgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGps = findViewById(R.id.txtGps);
        imgMap = findViewById(R.id.imgMap);

        callLocate();

    }

    public void callLocate(){
        // 안드로이드의 위치를 관리하는 객체가 LocationManager 다.
        // 따라서 안드로이드 시스템의 로케이션 서비스를 이 앱이 사용하겠다고
        // 1. 로케이션 서비스를 요청하고, 2. 로케이션 매니저 변수에 저장해줘야 한다.
        // 1) 로케이션 매니저 먼저 초기화, 그 다음 리스너를 생성
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 폰의 위치가 바뀔 때마다 해주고 싶은 액션을 여기에 작성.
                // 안드로이드가 폰의 위치가 바뀔 때마다 이 메소드를 호출한다는 뜻임.
                // 1. 위치가 바뀐 것을 감지해야 하므로, 바뀐 정보를 먼저 가져와야 한다.
                Log.i("Location", location.toString());
                double lat = location.getLatitude(); // 지도에서 위도 정보 뽑아오기
                double lng = location.getLongitude(); // 지도에서 경도 정보 뽑아오기

                String url = baseURL+"&center="+lng+","+lat; // 지도의 중심 위치 설정

                GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                        .addHeader("X-NCP-APIGW-API-KEY-ID",clientID)
                        .addHeader("X-NCP-APIGW-API-KEY",clientSecret).build());
                Glide.with(MainActivity.this).load(glideUrl).into(imgMap);
                txtGps.setText("Center : "+lng+" / "+lat);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // GPS 의 상태가 바뀌었을 때(아래의 두개와 같이 실행된다). 곧 삭제될 예정.
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
            // 권한 좀 쥬라
            // 3) 유저한테 이 앱은 위치기반 권한이 있어야 한다고 알려야 한다.(아래 코드)
            // 유저가 권한 설정을 하고 나면, 처리해야 할 코드를 작성하기 위해서 requestCode 값을 설정한다.
            // 이 코드를 쓰면, 앱을 켰을 때 유저에게 위치 사용 권한을 묻는다.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // 2) 로케이션 매니저와 로케이션 리스너를 연결해줘야 한다
        // 1:LocationManager.GPS_PROVIDER, 2:위치 갱신하는 기준 시간(밀리세컨 - 1000이 1초),
        // 3:위치 갱신하는 기준 거리(미터) - 00 쓰면 안됨. 계속 호출함, 4:로케이션 리스너 변수 입력
        // 아래 거 다 적고 Alt+Enter 할 때 처음에는 add permission ACCESS_FINE_LOCATION 을 선택하고, 그리고 한 번 더 Alt+Enter 해서 add permission check 를 선택해줘야 한다.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0,
                locationListener);
    }
    // 4) 유저가 뭘 눌렀을 때
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 유저가 권한을 허용해주면(allow)
        if (requestCode == 0) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0,
                    locationListener);
        }
    }
}
