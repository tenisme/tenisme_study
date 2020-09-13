package com.tenisme.android_java_background_lecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tenisme.android_java_background_lecture.repository.NumberRepository;
import com.tenisme.android_java_background_lecture.repository.RepositoryCallback;

// 이 뷰모델은 핸들러와 executor 를 받아야 한다.
// 이 두가지를 얻으려면 App 객체를 알아야 하고, App 을 알려면 Application 객체가 필요하다.
// 그러므로 그냥 ViewModel 을 사용하지 않고 AndroidViewModel 을 사용한다.
public class MainViewModel extends AndroidViewModel {

    private final NumberRepository repository;

    public MutableLiveData<Integer> progressLiveData = new MutableLiveData<>(0);

    public MainViewModel(@NonNull Application application) {
        super(application);

        // Application 객체를 가져옴으로서 NumberRepository 를 사용하기 위해 보내야 하는 값들
        // (핸들러와 Executor)을 App 객체로 형변환해서 파라미터로 보낼 수 있다.
        repository = new NumberRepository(
                ((App) application).mainThreadHandler,
                ((App) application).executorService
        );
    }

    // 오래되는 처리를 하고 중간중간에 UI 쓰레드에서 UI 갱신하려고 넘겨주는 값을 가지고 UI 처리를 넘겨줘야 함
    public void longTask() {
        repository.longTask(result -> {
            if(result instanceof Result.Success) {
                // 이 부분은 메인 쓰레드이기 때문에 setValue 를 쓰면 에러가 남.
                progressLiveData.postValue(((Result.Success<Integer>) result).data);
            }else if(result instanceof Result.Error){
                // 에러 처리
//                Toast.makeText(TAG, ((Result.Error<Integer>) result).exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
