package com.tenisme.android_java_background_lecture;

// T : 데이터 타입을 가리킴(어떠한 객체도 받을 수 있음)
public abstract class Result<T> {
    // 추상 클래스라서 기본 생성자는 생성할 수 없도록 막혀있음.
    private Result() {}

    //
    public static final class Success<T> extends Result<T> {
        public T data;

        public Success(T data) {
            this.data = data;
        }
    }

    public static final class Error<T> extends Result<T> {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}