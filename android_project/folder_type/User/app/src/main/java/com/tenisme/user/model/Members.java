package com.tenisme.user.model;

// DB를 묶어서 처리하기 위해 model 파일을 생성.
public class Members {
    private String id;
    private String email;
    private String password;

    public Members() {
    }

    public Members(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
