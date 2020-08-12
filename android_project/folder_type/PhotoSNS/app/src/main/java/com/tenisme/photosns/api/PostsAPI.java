package com.tenisme.photosns.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

// 인터페이스 = 선언
public interface PostsAPI {

    // 여기가 선언. GET 을 이렇게 보낼 것이다
    // Call 을 보내서 ResponseBody 로 받겠다.
    @GET("/api/v1/posts")
    Call<ResponseBody> getPosts(@Header("Authorization") String token,
                                @Query("offset") int offset,
                                @Query("limit") int limit);

}
