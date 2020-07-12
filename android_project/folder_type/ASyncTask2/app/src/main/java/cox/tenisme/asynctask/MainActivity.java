package cox.tenisme.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/2000px-Android_robot.svg.png";

                // Glide 써먹기
                Glide.with(MainActivity.this).load(url).into(img);
                    // .centerCrop() 이미지를 가운데에 추가하고 잘라내기
                    // .placeholder(여기) 안에 디폴트 이미지를 추가

//                DownloadImageTask downloadImageTask = new DownloadImageTask(MainActivity.this);
//                downloadImageTask.execute(url);
            }
        });
    }

    // 이미지를 네트워크를 통해서 다운로드해 화면에 표시할 쓰레드
    // 쓰레드란? 동시에 여러가지 작업을 가능하게 해주는 것. 새로운 쓰레드를 만들어준다.
    // 이러한 쓰레드를 사용하기 편하게 안드로이드에서 제공해주는 클래스가 AsyncTask 이다.
        // AsyncTask<여기> 설정하기 : <doInBackground() 의 파라미터, onProgressUpdate 의 파라미터, doInBackground() 의 리턴값>
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        // 4. 멤버변수 설정
        ProgressBar progressBar;
        Context context;

//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }

        // 5. context 만 가져오는 생성자 만듦
        public DownloadImageTask(Context context) {
            this.context = context;
            // 6. 프로그래스 바 이 액티비티에 새로 가져오기
//            progressBar = new ProgressBar(context); // 방법1 : xml 뷰에 배치 없이 메인 액티비티 xml 에서 이 프로그레스 바가 보이게 됨
            progressBar = findViewById(R.id.progressBar); // 방법2 : xml 뷰에 배치한 상태에서 뷰 가져오기
        }

        // 2. 이 메소드 만듦
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 아래 doInBackground 함수를 실행하기 전에, 해야할 일을 여기에 작성한다.

            // 7. 프로그래스 바 설정하기 : 프로그래스 바가 보이도록 하겠다
            progressBar.setVisibility(View.VISIBLE);
        }

        // 1. 알아서 생성된 메소드
        // "오래걸리는 일(작업)"이나, "동시 작업"이 필요한 일은 이 함수 안에 작성한다.
        // AsyncTask<>를 상속받아 쓰레드를 쓸 수 있게 된다.
            // String... > aaaa("aaa", "bbb"); > 문자열을 ,(콤마)로 무작위 개수로 호출할 때 쓴다
            // 이 함수 안에서는 배열 strings[인덱스]로 불러내면 된다.
        @Override
        protected Bitmap doInBackground(String... strings) {

            String url = strings[0];
            Bitmap bitmap = null;

            // 네트워크를 통해서 이미지 파일을 비트맵(Bitmap)으로 가져오는 코드
            try {
                URL getUrl = new URL(url);
                InputStream inputStream = getUrl.openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 리턴값을 null 에서 위에서 가져온 bitmap 파일로 바꿔주기
            return bitmap;
        }

        // 3. 이 메소드 만듦
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 위의 doInBackground 함수가 다 실행되고 나서 해야 할 일을 여기에 작성.

            progressBar.setVisibility(View.GONE); // 프로그래스 바를 사라지게 만듦
            img.setImageBitmap(bitmap); // Bitmap 형식으로 이미지를 받았으므로 .setImageResource 가 아니라 .setImageBitmap();을 써야한다.
        }
    }

}