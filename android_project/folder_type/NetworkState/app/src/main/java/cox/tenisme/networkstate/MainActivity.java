package cox.tenisme.networkstate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                int status_info = getNetworkStatus2(MainActivity.this);
                if(status_info == 1){
                    // 1은 wifi
                    imageView.setImageResource(R.drawable.wireless);
                }else if(status_info == 0) {
                    // 0은 mobile (4G, 5G 등등임)
                    imageView.setImageResource(R.drawable.smartphone);
                }else{
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        });

    }

//    // 네트워크 연결 여부를 확인해주는 함수 템플릿 1 : 구버전
//    public int getNetworkStatus(Context context){
//        // 안드로이드 서비스에 폰에서 네트워크 연결 관리하는 애를 하나 가져와달라고 요청한다
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if(networkInfo != null) {
//            switch (networkInfo.getType()) {
//                case ConnectivityManager.TYPE_WIFI:
//                    return 1;
//                case ConnectivityManager.TYPE_MOBILE:
//                    return 0;
//                default:
//                    return 3;
//            }
//        }else{
//            return 1000;
//        }
//    }

    // 네트워크 연결 여부를 확인해주는 함수 템플릿 2 : version_Q 호환
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getNetworkStatus2(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return 3;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return 1;
                }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    return 2;
                }
            }
        } else {
            android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                switch (networkInfo.getType()){
                    case ConnectivityManager.TYPE_WIFI:
                        return 1;
                    case ConnectivityManager.TYPE_MOBILE:
                        return 0;
                    default:
                        return 3;
                }
            }else{
                return 1000;
            }
        }
        return 1000;
    }

}