package com.tenisme.android_java_background_lecture;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    // 현재 실행중인 프로세스가 사용할 수 있는 프로세서의 개수를 리턴해줌.
    // 리턴해주는 개수 만큼의 쓰레드를 사용하겠다. 매번 같은 값이 아닐 수 있음. 현재 상태의 프로세서 개수를 얻기 때문임.
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // ExecutorService : 앱 안의 백그라운드 실행을 위해서 사용하는 객체.
    //                   앱 전체에서 ExecutorService 를 사용해서 백그라운드 처리를 한다.
    //                   그때그때 Thread 를 생성하는 것이 아니라 전체적으로 Thread 를 관리하는 객체(를 작성한다).
    //                   (메인 쓰레드가 아닌) 백그라운드 쓰레드에서 동작할 수 있는 Executor 를 만들어줌.
    // newFixedThreadPool : Thread fool 을 n개를 사용하겠다. 동시에 n가지 백그라운드 작업을 할 수 있도록 만든다.
    //                      n개를 다 쓰는 상태일 때에는 대기하고 있다가 유휴 Thread 가 생기면 동작한다.
    // nThread : 사용할 쓰레드 개수
    ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);

    // mainThreadHandler : helper 의 역할을 함. (백그라운드 쓰레드와 서로 방해하지 않는) 메인쓰레드 핸들러를 만들어줌.
    //                     메인 쓰레드 핸들러를 앱 안에서 같이 사용을 함.
    // Looper : 메인쓰레드를 반복시켜주는 객체. MainLooper 는 새로운 UI 갱신이 들어왔을 때 실행을 해주는 역할을 한다.
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

}
