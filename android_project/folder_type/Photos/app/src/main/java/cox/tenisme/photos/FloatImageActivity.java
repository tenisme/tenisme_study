package cox.tenisme.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cox.tenisme.photos.model.Photos;

public class FloatImageActivity extends AppCompatActivity {

    ImageView bigImage;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_image);

        Log.i("Photos","into FloatImage");

        bigImage = findViewById(R.id.bigImage);

        i = getIntent();

        String url = i.getStringExtra("goToBig");
        Log.i("Photos",url);

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent","Your-User-Agent").build());

        Glide.with(FloatImageActivity.this).load(glideUrl).optionalCenterInside()
                .placeholder(android.R.drawable.ic_menu_help).into(bigImage);

    }
}