package com.block.photoapp.model;

import com.google.gson.annotations.SerializedName;

public class UserRes {

    // Gson 으로 처리하는 클래스
    // 네트워크를 통해 gson 으로 받아오는 것을 알아서 여기에 담아줌(처리해줌).
    // @SerializedName 에는 실제로 json 으로 받아오는(res) 키 이름을 셋팅한다.
    @SerializedName("success")
    boolean success;

    @SerializedName("token")
    String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
