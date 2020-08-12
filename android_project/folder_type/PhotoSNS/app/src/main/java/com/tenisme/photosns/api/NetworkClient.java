package com.tenisme.photosns.api;

import android.content.Context;

import com.tenisme.photosns.utils.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static Retrofit retrofit;

    // 객체 생성 안하고 호출할 수 있는(static) 함수 만듦.
    public static Retrofit getRetrofitClient(Context context){

        if(retrofit == null){
            OkHttpClient httpClient = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder().baseUrl(Utils.BASE_URL).client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
