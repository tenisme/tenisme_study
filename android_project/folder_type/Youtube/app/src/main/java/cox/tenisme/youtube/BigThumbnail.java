package cox.tenisme.youtube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static cox.tenisme.youtube.MainActivity.URL;

public class BigThumbnail extends AppCompatActivity {

    ImageView imgBigThumbnail;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_thumbnail);

        imgBigThumbnail = findViewById(R.id.imgBigThumbnail);

        i = getIntent();

        String thumbnailBigUrl = i.getStringExtra("thumbnailBigUrl");

        Glide.with(BigThumbnail.this).load(thumbnailBigUrl).optionalCenterInside()
                .placeholder(android.R.drawable.ic_dialog_alert).into(imgBigThumbnail);

    }
}