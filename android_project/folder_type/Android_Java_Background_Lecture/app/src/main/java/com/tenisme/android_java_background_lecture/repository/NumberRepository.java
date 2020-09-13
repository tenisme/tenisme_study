package com.tenisme.android_java_background_lecture.repository;

import android.os.Handler;

import com.tenisme.android_java_background_lecture.Result;

import java.util.concurrent.Executor;

// 숫자 데이터를 제공하는 클래스
public class NumberRepository {

    // 레파지토리가 숫자를 계속 발생시키면서 중간중간 UI를 계속 갱신할 수 있도록
    // Handler 와 Executor 를 다 가지고 있어야 함.
    // final 이 없으면 전역변수가 null 이 될 수 있기 때문에 그걸 방지하기 위해서 final 을 붙임.
    //      의존성 주입을 위해서 해줌.
    private final Handler resultHandler;
    private final Executor executor;

    // 오래 걸리는 처리를 하려면 생성자에서 Executor 를 제공받아야한다.
    public NumberRepository(Handler resultHandler, Executor executor) {
        this.resultHandler = resultHandler;
        this.executor = executor;
    }

    // 오래걸리는 처리를 하는 메소드. 숫자를 쭉 발생시켜주는 역할. 콜백을 활용하는 방법임.
    // 콜백을 받아서 활용해야 하기 때문에 함수를 정의할 떼 파라미터에 RepositoryCallback 을 입력한다
    // Integer 타입으로 callback 할 수 있도록 만든다.
    public void longTask(final RepositoryCallback<Integer> callback) {
        // executor 안에 runnable 를 지정해놓으면 이 코드가 알아서 자체 Thread(execute) 로 실행이 된다.
        // 오래 걸리는 처리를 해야 하기 때문에 executor 에서 실행한다.
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // background 에서 수행될 코드
                    int num = 0;
                        for (int i = 0; i < 100; i++) {
                            num++;
                            // UI 갱신을 위해서 콜백
                            Result<Integer> result = new Result.Success<>(num);
                            // 이렇게 해야 제대로 콜백이 전달이 된다. (잘 전달돼서 앱이 안 죽는다)
                            notifyResult(result, callback);
                            // 백그라운드 쓰레드에 슬립을 건다 (0.1초)
                            Thread.sleep(100);
                    }
                }catch(Exception e){
                    Result<Integer> result = new Result.Error<>(e);
                    notifyResult(result, callback);
                }
            }
        });
    }

    // Callback 을 실행하려는 메소드
    // 실행 결과와 콜백 객체를 같이 넘겨주고 핸들러를 통해서 콜백을 할 수 있게 한다
    // UI를 백그라운드(longTask)에서 갱신시키려고 하면 앱이 죽기 때문에 따로 메소드를 만들어야 한다.
    private void notifyResult(final Result<Integer> result, final RepositoryCallback<Integer> callback) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                // resultHandler 에 Runnable 을 써주면 쓰레드 백그라운드 안에서
                // 순간순간 UI 쓰레드로 이(아래) 데이터에만 UI를 별도 쓰레드로 쏜다(처리를 한다) <- 받아적은 건데 뭔..
                callback.onComplete(result);
            }
        });
    }
}
