package com.tenisme.android_java_background_lecture.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenisme.android_java_background_lecture.R;
import com.tenisme.android_java_background_lecture.databinding.FragmentJobBinding;
import com.tenisme.android_java_background_lecture.service.JobService;

public class JobFragment extends Fragment {

    private FragmentJobBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        binding = FragmentJobBinding.bind(view);
        return binding.getRoot(); // view 를 리턴해도 상관없음. 같은 것임.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobService.enqueueWork(requireContext(), new Intent());
            }
        });
    }
}