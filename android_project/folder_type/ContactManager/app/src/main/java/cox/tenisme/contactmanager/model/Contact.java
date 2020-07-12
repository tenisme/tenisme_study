package cox.tenisme.contactmanager.model;

// 데이터를 저장하는 클래스
public class Contact {
    private int id;
    private String name;
    private String phoneNumber;

    // 그냥 생성자
    public Contact(){

    }

    // 데이터 저장하는 생성자
    public Contact(int id, String name, String phoneNumber){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // 게터 세터 만들기

    // id 게터세터
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    // name 게터세터
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // phoneNumber 게터세터
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
