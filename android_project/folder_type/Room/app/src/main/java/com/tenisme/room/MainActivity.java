package com.tenisme.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tenisme.room.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 바인딩으로 레이아웃을 가져옴 -> 해당 xml 에 대한 객체 정보를 바인딩이 전부 가지고 있음.
        // setContentView(), findViewById()를 대체할 수 있음.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // LiveData 가 관찰하는 것(지금은 DB 변경 정보)이 xml 에 반영이 바로바로 되게 만듦.
        binding.setLifecycleOwner(this);

        // 뷰모델 가져오기
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // 뷰모델이 바인딩 객체 xml 로 들어감(뷰모델.class 와 xml 설정(<data><variable ~ /></data>) 연결)
        // -> xml 파일에서 viewModel.class 안의 함수와 변수를 가져와서 쓸 수 있게 됨.
        binding.setViewModel(viewModel);
    }
}