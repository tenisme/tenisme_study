package com.block.photoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.block.photoapp.api.NetworkClient;
import com.block.photoapp.api.PostApi;
import com.block.photoapp.model.UserRes;
import com.block.photoapp.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Posting extends AppCompatActivity {

    Button btnCamera;
    Button btnGallery;
    ImageView imgPhoto;
    EditText editContent;
    Button btnPosting;

    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        imgPhoto = findViewById(R.id.imgPhoto);
        editContent = findViewById(R.id.editContent);
        btnPosting = findViewById(R.id.btnPosting);

        btnCamera.setOnClickListener(view -> {
            // 권한 요청하는 방법
            int permissionCheck = ContextCompat.checkSelfPermission(
                    Posting.this, Manifest.permission.CAMERA);

            // 퍼미션 승낙이 안 되어있는 경우 권한 요청하기
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Posting.this,
                        new String[]{Manifest.permission.CAMERA}, 1000);
                Toast.makeText(Posting.this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // 이미지 캡쳐 해죠라(사진 찍겠다) - 앱 하나면 그거 켜지고 다양하면 앱 선택
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 폰에 카메라 앱이 있으면
                if (i.resolveActivity(Posting.this.getPackageManager()) != null) {
                    // 사진의 파일명을 만들기
                    String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    photoFile = getPhotoFile(fileName);

                    // 파일 처리하기 : 현재 액티비티, 패키지명, 파일을 입력. fileProvider 보내줄테니까 사진 찍으면 여기다 이 파일명 사진 저장좀 해죠라.
                    Uri fileProvider = FileProvider.getUriForFile(Posting.this,
                            "com.block.photoapp.fileprovider", photoFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                    startActivityForResult(i, 100);

                } else {
                    // 폰에 카메라 앱이 없으면
                    Toast.makeText(Posting.this, "카메라 앱이 없음", Toast.LENGTH_SHORT);
                    return;
                }
            }
        });
        btnGallery.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    displayFileChoose();

                } else {
                    requestPermission();
                }

            }
        });
        btnPosting.setOnClickListener(view -> {
            String content = editContent.getText().toString().trim();

            if(content.isEmpty() || photoFile == null){
                Toast.makeText(Posting.this, "모든 항목을 전부 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Retrofit retrofit = NetworkClient.getRetrofitClient(Posting.this);

            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
            MultipartBody.Part part = MultipartBody.Part.createFormData("photo",
                    photoFile.getName(), fileBody);

            RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), content);

            PostApi postApi = retrofit.create(PostApi.class);

            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
            String token = sp.getString("token", null);

            Call<UserRes> call = postApi.createPost("Bearer "+token, part, textBody);

            call.enqueue(new Callback<UserRes>() {
                @Override
                public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                    if(response.isSuccessful()){
                        if(response.body().isSuccess()){
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRes> call, Throwable t) {

                }
            });
        });
    }

    // 고화질 사진 가져오기
    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 직접 파일 가져오기 함수들(아래 세개)
    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Posting.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Posting.this, "권한 수락이 필요합니다", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(Posting.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
            // 위 리퀘스트 코드는 겹치면 안되므로 나중에 상수처리한다.
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Posting.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED) {
            return true;
        } else {
            return false;
        }
    }

    // 퍼미션(권한 요청) OK가 들어올 때의 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Posting.this, "권한 허가되었음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Posting.this, "권한이 승인되지 않음", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Posting.this, "권한 허가되었음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Posting.this, "권한이 승인되지 않음", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // intent 로 돌려받는 값
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 100 & resultCode == RESULT_OK) {
            // 화소가 떨어지는 이미지를 돌려받기
            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imgPhoto.setImageBitmap(photo);
        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null && data.getData() != null){
            // 이미지 저장소에서 직접 파일 가져오기
            Uri imgPath = data.getData();
            photoFile = new File(imgPath.getPath());
            imgPhoto.setImageURI(imgPath);

            // 실제 경로를 몰라도, 파일의 내용을 읽어와서, 임시파일 만들어서 서버로 보낸다.
            String id = DocumentsContract.getDocumentId(imgPath);
            try {
                InputStream inputStream = getContentResolver().openInputStream(imgPath);
                photoFile = new File(getCacheDir().getAbsolutePath()+"/"+id+".jpg");
                writeFile(inputStream, photoFile);
//                String filePath = photoFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 파일의 내용을 읽어와서, 임시파일 만들기 위함.
    void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}