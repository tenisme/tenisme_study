package com.tenisme.android_java_background_lecture.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenisme.android_java_background_lecture.R;
import com.tenisme.android_java_background_lecture.WorkManagerViewModel;
import com.tenisme.android_java_background_lecture.databinding.FragmentJobBinding;
import com.tenisme.android_java_background_lecture.databinding.FragmentWorkBinding;
import com.tenisme.android_java_background_lecture.worker.MyWorker;

public class WorkFragment extends Fragment {

    private FragmentWorkBinding binding;
    private WorkManagerViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        binding = FragmentWorkBinding.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // WorkManagerViewModel 의 생명주기를 액티비티에 의존하기 위해 requireActivity() 로 설정한다.
        // 그래야 WorkFragment 가 파괴되고 다시 생성이 되어도 뷰모델은 살아있다.
        // 이렇게 해야 이전에 실행했던 데이터를 계속 연동해서 사용할 수 있다.
        viewModel = new ViewModelProvider(requireActivity()).get(WorkManagerViewModel.class);

        viewModel.progressLiveData.observe(getViewLifecycleOwner(), workInfo -> {
            // 상태가 변경될 때마다 UI를 여기서 갱신한다
            binding.progress.setProgress(workInfo.getProgress().getInt("progress", 0));
        });

        binding.button.setOnClickListener(v -> viewModel.startLongTask());

//        // getViewLifecycleOwner() : 현재 프레그먼트의 생명 주기에 맞춰서
//        // workInfo -> {} : 이 값(workInfo)을 얻겠다
//        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request.getId())
//                .observe(getViewLifecycleOwner(), workInfo -> {
//                    int progress = workInfo.getProgress().getInt("progress", 0);
//                    binding.progress.setProgress(progress);
//                });
    }
}