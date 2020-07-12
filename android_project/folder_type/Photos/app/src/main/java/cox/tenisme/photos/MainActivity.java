package cox.tenisme.photos;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cox.tenisme.photos.adapter.RecyclerViewAdapter;
import cox.tenisme.photos.model.Photos;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    public static final String URL = "https://jsonplaceholder.typicode.com/photos";

    Photos photos;
    ArrayList<Photos> photosArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("Photos", "Start onCreate");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Photos", "response : " + response.toString());

                        for (int i = 0 ; i < response.length() ; i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                int albumId = object.getInt("albumId");
                                int id = object.getInt("id");
                                String title = object.getString("title");
                                String url = object.getString("url");
                                String thumbnailUrl = object.getString("thumbnailUrl");

//                                Log.i("Photos",albumId+" / "+id +" / "+title+" / "+url+" / "+thumbnailUrl);

                                photos = new Photos(albumId,id,title,url,thumbnailUrl);
                                photosArrayList.add(photos);

//                                Log.i("Photos", i+"번째 데이터 : "+
//                                        photosArrayList.get(i).getAlbumId()
//                                        +" / "+photosArrayList.get(i).getId()
//                                        +" / "+photosArrayList.get(i).getTitle()
//                                        +" / "+photosArrayList.get(i).getUrl()
//                                        +" / "+photosArrayList.get(i).getThumbnailUrl());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, photosArrayList);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Photos","Error : "+error.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}