package com.tenisme.android_java_background_lecture.repository;

import com.tenisme.android_java_background_lecture.Result;

// Repository 의 자체 Callback 을 생성하고 Callback 을 통해서 실행함
// 한 파일 안에 public 객체가 두 개 있으면 안 되므로 NumberRepository 안에 있던 아래 코드를 별도 파일로 따로 빼준다.
public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}