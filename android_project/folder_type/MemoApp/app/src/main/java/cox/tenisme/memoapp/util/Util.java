package cox.tenisme.memoapp.util;

public class Util {
    public static final String DATABASE_NAME = "memo_db";
    public static final int DATABASE_VERSION = 1;

    // 테이블 이름 지정
    public static final String TABLE_NAME = "memos";

    // 컬럼 이름 지정 (당근 무조건 String) 컬럼 안에 데이터 저장하는 거 아님 주의
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "name";
    public static final String KEY_MEMO = "memo";
}
