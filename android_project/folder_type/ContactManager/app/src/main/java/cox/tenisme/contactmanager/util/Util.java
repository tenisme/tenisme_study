package cox.tenisme.contactmanager.util;

// 데이터 간편 처리, 하드 코딩을 하지않기 위한 상수 지정 클래스
public class Util {
    public static final String DATABASE_NAME = "contact_db";
    public static final int DATABASE_VERSION = 1;

    // 테이블 이름 지정
    public static final String TABLE_NAME = "contacts";

    // 컬럼 이름 지정 (당근 무조건 String) 컬럼 안에 데이터 저장하는 거 아님 주의
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NUMBER = "phone_number";
}
