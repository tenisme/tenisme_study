package com.tenisme.android_java_background_lecture.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tenisme.android_java_background_lecture.MainViewModel;
import com.tenisme.android_java_background_lecture.R;
import com.tenisme.android_java_background_lecture.Result;
import com.tenisme.android_java_background_lecture.databinding.FragmentExecutorBinding;
import com.tenisme.android_java_background_lecture.repository.RepositoryCallback;

public class ExecutorFragment extends Fragment {

    private static final String TAG = ExecutorFragment.class.getSimpleName();
    // 뷰모델을 가져다 쓰기
    private MainViewModel viewModel;
    // 바인딩 객체 가져오기 : 이 파일에서 에서 inflater 로 가져온 프래그먼트 이름 + Binding
    private FragmentExecutorBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_executor, container, false);
        // 상단 뷰 객체를 바인딩 객체와 연결(위 프레그먼트의 객체(버튼 등) 속성들을 가져올 수 있음)
        binding = FragmentExecutorBinding.bind(view);
        return binding.getRoot();
    }

    // onViewCreated : 화면이 다 만들어진 상태
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 자바에서 new ViewModelProvider 로 AndroidViewModel(을 상속한 클래스) 을 가져오려면
        //      첫 번째 생성자 파라미터 뒤에 팩토리를 지정해줘야 한다 (new ViewModelProvider.~)
        // 이 프레그먼트와 관계된 뷰모델을 쓰려면 this 를 쓰고, 액티비티 쪽 뷰모델을 가져오려면 requireActivity() 를 써야한다
        // 프레그먼트가 파괴되고 다시 생성되도 여기 있는 데이터는 계속 유지됨.
        viewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(MainViewModel.class);

        // getViewLifecycleOwner() : 이 프레그먼트의 라이프사이클 동안에만(값이 갱신되는 동안에만)
        // new Observer<Integer progress>(progress -> {}) : 콜백처럼 동작함
        viewModel.progressLiveData.observe(getViewLifecycleOwner(), progress ->
                // 변환이 될 때마다 여기에 결과(Integer)가 넘어올 것
                binding.progress.setProgress(progress));

        binding.button.setOnClickListener(v -> viewModel.longTask());
    }
}