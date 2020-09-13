package com.tenisme.android_java_background_lecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.tenisme.android_java_background_lecture.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // activity_main.xml 에 가져다놓은 nav_host_fragment 를 가져와서 연결함
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // nav_graph.xml 에 정의된 각 프래그먼트의 id 들을 가져와서 버튼에 연결함
        binding.btnExecutor.setOnClickListener(v -> navController.navigate(R.id.executorFragment));
        binding.btnJob.setOnClickListener(v -> navController.navigate(R.id.jobFragment));
        binding.btnWork.setOnClickListener(v -> navController.navigate(R.id.workFragment));

    }
}