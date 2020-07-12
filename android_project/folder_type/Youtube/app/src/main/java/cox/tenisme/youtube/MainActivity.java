package cox.tenisme.youtube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import cox.tenisme.youtube.adapter.RecyclerViewAdapter;
import cox.tenisme.youtube.model.Youtube;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    public static final String URL = "https://www.googleapis.com/youtube/v3/search" +
            "?part=snippet&key=AIzaSyDZTBpF7-q9LiD9zvVi8nscaCof8-PgbpY&maxResults=20&type=video";

    JSONArray itemsArray;
    JSONObject objectInItems;
    JSONObject idObject;
    JSONObject snippetObject;
    JSONObject thumbnailsObject;
    JSONObject defaultObject;
    JSONObject highObject;

    Youtube youtube;
    ArrayList<Youtube> youtubeArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    EditText editSearch;
    Button btnSearch;

    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("YTB", "Start onCreate");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 검색 버튼을 누르면 해당 검색어를 API 를 통해 받아온다
                // 2. 받아온 API 를 어레이리스트에 저장하고
                // 3. 저장한 리스트를 리사이클러뷰로 보여준다

                // 검색어 String 으로 받아오기
                search = "&q="+editSearch.getText().toString().trim();

                // 저장된 어레이리스트에 내용이 있으면 리사이클러 뷰 화면을 초기화한다
                if(search.equals("&q=")){
                    Toast.makeText(MainActivity.this,"검색어를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(youtubeArrayList.size() > 0){
                    youtubeArrayList.clear();
                }
                // 검색어를 URL 에 추가해 API 로 불러오고, 리사이클러뷰에 표시
                getJSON();
            }
        });

    }

    public void getJSON(){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL+search, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("YTB", "Response : " + response.toString());

                        // itemsArray 가져오기
                        try {
                            itemsArray = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                                Log.i("YTB", itemsArray.toString());

                        for (int i = 0; i < itemsArray.length(); i++) {
                            try {
                                // itemsArray 에서 각각의 오브젝트들을 뽑아오기
                                objectInItems = itemsArray.getJSONObject(i);
//                                Log.i("YTB",i+" "+objectInItems.toString());

                                // 뽑아온 object 에서 필요한 것들 가져오기
                                // objectInItems > id > videoId
                                // objectInItems > snippet > title
                                // objectInItems > snippet > description
                                // objectInItems > snippet > thumbnails > default > url
                                // objectInItems > snippet > thumbnails > high > url
                                idObject = objectInItems.getJSONObject("id");
                                snippetObject = objectInItems.getJSONObject("snippet");
                                thumbnailsObject = snippetObject.getJSONObject("thumbnails");
                                defaultObject = thumbnailsObject.getJSONObject("default");
                                highObject = thumbnailsObject.getJSONObject("high");
//                                Log.i("YTB", i+" "+idObject.toString());

                                // videoId 가져오기
                                String videoId = idObject.getString("videoId");
                                // title 가져오기
                                String title = snippetObject.getString("title");
                                // description 가져오기
                                String description = snippetObject.getString("description");
                                // 썸네일 url 2가지 가져오기
                                String thumbnailUrl = defaultObject.getString("url");
                                String thumbnailBigUrl = highObject.getString("url");
//                                Log.i("YTB", i+" "+videoId);

                                youtube = new Youtube(videoId, title, description, thumbnailUrl, thumbnailBigUrl);
                                youtubeArrayList.add(youtube);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, youtubeArrayList);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("YTB", "Error : " + error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}