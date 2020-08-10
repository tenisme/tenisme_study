package com.tenisme.mycamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnCamera;
    Button btnGallery;
    ImageView image;

    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        image = findViewById(R.id.image);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 권한 요청하는 방법
                int permissionCheck = ContextCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.CAMERA);

                // 퍼미션 승낙이 안 되어있는 경우 권한 요청하기
                if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1000);
                    Toast.makeText(MainActivity.this,"카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    // 이미지 캡쳐 해죠라(사진 찍겠다) - 앱 하나면 그거 켜지고 다양하면 앱 선택
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 폰에 카메라 앱이 있으면
                    if(i.resolveActivity(MainActivity.this.getPackageManager()) != null){
                        // 사진의 파일명을 만들기
                        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        photoFile = getPhotoFile(fileName);

                        // 파일 처리하기 : 현재 액티비티, 패키지명, 파일을 입력. fileProvider 보내줄테니까 사진 찍으면 여기다 이 파일명 사진 저장좀 해죠라.
                        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this,
                                "com.tenisme.mycamera.fileprovider", photoFile);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        startActivityForResult(i, 100);

                    }else{
                        // 폰에 카메라 앱이 없으면
                        Toast.makeText(MainActivity.this,"카메라 앱이 없음",Toast.LENGTH_SHORT);
                        return;
                    }
                }

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    // 퍼미션(권한 요청) OK가 들어올 때의 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1000 : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "권한 허가되었음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "권한이 승인되지 않음", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    // intent 로 돌려받는 값
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 100 & resultCode == RESULT_OK){
            // 화소가 떨어지는 이미지를 돌려받기
            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            image.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}