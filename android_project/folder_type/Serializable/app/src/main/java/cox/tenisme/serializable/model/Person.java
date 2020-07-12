package cox.tenisme.serializable.model;

import java.io.Serializable;

// 정보를 저장하고 intent 로 정보를 전달할 클래스 생성
// implements Serializable 추가
    // Serializable : "직렬"
    // 데이터를 바이트단위로 일렬로 직렬(나열)하라
public class Person implements Serializable {

    private String name;
    private String email;
    private boolean isMale;

    public Person() {

    }

    public Person(String name, String email, boolean isMale) {
        this.name = name;
        this.email = email;
        this.isMale = isMale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

}
