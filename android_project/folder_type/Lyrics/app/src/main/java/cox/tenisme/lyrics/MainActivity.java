package cox.tenisme.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText editArtist;
    EditText editSong;
    Button btnApi;
    TextView txtLyrics;

    String requestUrl = "https://api.lyrics.ovh/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editArtist = findViewById(R.id.editArtist);
        editSong = findViewById(R.id.editSong);
        btnApi = findViewById(R.id.btnApi);
        txtLyrics = findViewById(R.id.txtLyrics);

        btnApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1. 에디트 텍스트 두 개를 request URL 의 파라미터로 사용하기 위해 String 으로 가져온다
                String artist = editArtist.getText().toString();
                String song = editSong.getText().toString();

                // 2. 위 두개 문자열을 파라미터로 만든다
                String url = requestUrl + artist + "/" + song;
                Log.i("song", "url : " + url);

                // 2-2. 가수 이름이나 노래 제목을 적지 않았을 경우 토스트 띄우고 txtLyrics 초기화 시키기
                if(artist.isEmpty() || song.isEmpty()){
                    Toast.makeText(MainActivity.this, "가수이름과 노래 제목을 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    txtLyrics.setText("");
                    Log.i("song", "토스트 성공");
                }

                // 3. api 를 요청한다
                // Volley 라이브러리의 RequestQueue 클래스를 객체로 생성한다. context 는 항상 현재 액티비티를 적는다
                // 여기서는 MainActivity
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // onResponse 함수 : 정상적으로 api 호출이 완료되었을 때 호출되는 ★콜백★ 함수.
                        // api 서버가 보내주는 데이터는 response 변수에 실려서 옵니다.
                        // 여기서, 우리가 원하는 데이터를 파싱합니다(뽑아냅니다).
                        Log.i("song", "result : " + response.toString());
                        try {
                            txtLyrics.setText(response.getString("lyrics"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 서버에 문제가 생겨서 정상적으로 동작하지 않을 때 이 ★콜백★ 메소드가 호출됨.
                        // error 변수에 왜 에러가 났는지에 대한 메세지(데이터)가 실려온다.
                        // ((int) error.networkResponse.statusCode); ??????? ㅋㅋㅋㅋㅋㅋ
                    }
                });
                // 네트워크 호출하는 코드(이거 없으면 jsonObjectRequest 가 실행되지 않음)
                // requestQueue 와 jsonObjectRequest 를 연결해 네트워크를 호출하는 방법
                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}
