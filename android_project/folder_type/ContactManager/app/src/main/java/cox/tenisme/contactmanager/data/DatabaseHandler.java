package cox.tenisme.contactmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import cox.tenisme.contactmanager.model.Contact;
import cox.tenisme.contactmanager.util.Util;

// 데이터베이스를 다루는 클래스 (액티비티에서(유저가) 다룰 클래스)
// SQLiteOpenHelper 를 상속받는다
public class DatabaseHandler extends SQLiteOpenHelper {

    // 생성자
    // 액티비티에서 핸들러 객체를 생성하면 아래의 생성자가 먼저 호출되고 그 아래에 있는 onCreate 가 실행된다 = 테이블이 만들어진다
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. 테이블 작성은 여기 안에서
        // 우측 문자열 안에는 SQL 문을 작성한다. 빡시네ㅠㅠㅠㅠㅠㅠㅠ
        // 띄어쓰기에 주의할 것. int 대신에 integer, varchar 대신에 text 로, auto_increment 를 autoincrement 로 쓴다.
        // ★ MySQL 과 SQLite 는 문법이 조금씩 다르다. ★
        String CREATE_CONTACT_TABLE = "create table " + Util.TABLE_NAME + "(" + Util.KEY_ID
                + " integer not null primary key autoincrement, "
                + Util.KEY_NAME + " text,"
                + Util.KEY_PHONE_NUMBER + " text )";
        // 위 String 은 아래에서 작성한 것과 같다
        // create table contacts
        // ( id integer not null autoincrement primary key,
        // name text,
        // phone_number text)

        // 2. 쿼리 실행도 여기서 - 쿼리 실행은 ★ .execSQL(); ★
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 삭제
        String DROP_TABLE = "drop table " + Util.TABLE_NAME;
        db.execSQL(DROP_TABLE);

        // ★★★필수★★★ 테이블을 다시 생성한다
        onCreate(db);
    }

    // 주소 저장하는 메소드 : 오버라이딩이 아니라, 직접 만들어야 하는 메소드
    // insert 함수
    public void addContact(Contact contact) {
        // 1. 주소를 "저장"하기 위해 writable db를 가져온다.
            // SQLiteDatabase 로 db를 "열면" 깔끔한 코딩을 위해 db.close();로 꼭 닫아주자.
            // 실무에서는 SQLiteDatabase db;로 전역변수를 만들어서 쓴다고 한다.
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. db에 저장하기 위해서는 ContentValues 를 이용한다.
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());
        // 3. db에 실제로 저장한다.
        db.insert(Util.TABLE_NAME, null, values);
        db.close(); // insert 하면 항상 close 를 해준다.
        // 데이터 추가가 잘 처리됐는지 확인하는 Log 만들기
        Log.i("MyDB","interested");
    }

    // 주소 "1개" 가져오는 메소드 : 직접 만들어야 하는 메소드
    // ★ select * from contacts where id = 2; 이걸 만들겠다는 뜻 ★
        // id 로(id 조건으로) 셀렉트할 수 있는 함수를 만든다는 뜻.
    // Contact 타입(아까 만든 데이터 저장 클래스)을 리턴
    public Contact getContact(int id) {
        // 1. 데이터베이스 가져오기. 조회하는 거니까 readable 한 db로 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. 데이터를 셀레터(조회) 할 때는, Cursor - .query() 를 이용해야 한다.
        // (테이블 이름, 컬럼(여기서는 세 컬럼 다 조회), where 절, ?표 값이 무엇인지를 변수로, group by, having, order by)
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_NAME,
                        Util.KEY_PHONE_NUMBER}, Util.KEY_ID + " =?", new String[]{String.valueOf(id)},
                null, null, null);
        // 두가지 이상을 조회할 때(?가 여러개일 때) 예시
            // where 절 처리 : Util.KEY_ID + " =?, "+ Util.KEY_NAME + " = ?"
            // ?표 처리 : new String[]{String.valueOf(id), "Hello"}
        // new String[] {String.valueOf(id)} 에서 숫자값을 String 으로 바꿔준 이유는 db 문은 문자열로 써야하기 때문임.

        // select 한 데이터가 들어있으면(not null 이면)
        if (cursor != null) {
            cursor.moveToFirst(); // 커서(셀렉트한 것(들))의 첫번째(맨 위 데이터)로 이동해라
        }
        int selectedId = Integer.parseInt(cursor.getString(0));
        // 커서의 "컬럼 인덱스(여기서는 id가 0, name 이 1, phone_number 가 2이다)"가 0번째인 것을 문자열로 가져와서 int 타입으로 바꿔라
        String selectedName = cursor.getString(1);
        String selectedPhoneNumber = cursor.getString(2);

        // DB 에서 읽어온 데이터(위)를 자바 클래스로 처리한다
        Contact contact = new Contact(); // 여기에 정보를 담아준다
        contact.setId(selectedId);
        contact.setName(selectedName);
        contact.setPhoneNumber(selectedPhoneNumber);

        return contact;
    }

    // 이 테이블의 모든 데이터(모든 주소 정보)를 가져오는 함수 만들기
    // ★ 이걸 만든다 -> select * from contacts; ★
    // ArrayList 를 사용해 모든 데이터를 가져온다
    public ArrayList<Contact> getAllContacts() {
        // 1. 비어있는 어레이리스트를 먼저 한 개 만든다.
        // <Contact> : 이 리스트에는 Contact 클래스를 담겠다는 뜻
        ArrayList<Contact> contactList = new ArrayList<>();
        // 2. 데이터베이스에서 모든 데이터를 select(조회) 한다
        // "select" 하는 쿼리문을 만든다
        String selectAll = "select * from " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase(); // 읽는다 > read
        Cursor cursor = db.rawQuery(selectAll, null); // 여기의 null > where 가 필요없다는 뜻
        // 3. 여러 개의 데이터를 루프를 돌면서 Contact 클래스에 정보를 하나씩 담는다
        if (cursor.moveToFirst()) {
            do {
                // db 에서 가져온다
                int selectedId = Integer.parseInt(cursor.getString(0));
                String selectedName = cursor.getString(1);
                String selectedPhoneNumber = cursor.getString(2);

                Contact contact = new Contact(); // 새 클래스(주소)를 만들어서 읽어온 db 데이터들을 담는다
                contact.setId(selectedId);
                contact.setName(selectedName);
                contact.setPhoneNumber(selectedPhoneNumber);

                // 4. 비어있는 ArrayList 에 데이터를 하나씩 추가시킨다.
                contactList.add(contact);

            } while (cursor.moveToNext()); // 이걸 다음 데이터가 없을 때까지 계속 반복한다
        }
        return contactList;
    }

    // update 함수 - 데이터를 업데이트하는 메소드.
    public int updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        // 데이터베이스 테이블 업데이트
        // update contacts set name = "", phone = "" where id = ?;
        // db.update(테이블이름, 업데이트할 값들(values), where 절, ?에 들어가는 것)
        int ret = db.update(Util.TABLE_NAME, values, Util.KEY_ID+" = ?", new String[]{String.valueOf(contact.getId())});
        db.close(); // onDestroy()를 대신 쓸 때도 있음?? 여기서는 이게 맞다고 함.
        return ret;
    }

    // delete 함수 - 데이터를 지우는 메소드
    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(테이블이름, where 절, ?에 들어가는 것)
        db.delete(Util.TABLE_NAME, Util.KEY_ID+" = ?", new String[]{String.valueOf(contact.getId())});
        db.close(); // DB "작업" 종료(꼭 적어주기) // 이거 안 적어주면 그냥 앱 끌 때 디비도 알아서 꺼지긴 한다.
    }

    // 테이블에 저장된 주소록 데이터의 전체 개수를 리턴하는 메소드
    // select count(*) from contacts; 를 구현함
    public int getCount(){
        String countQuery = "select * from "+Util.TABLE_NAME; // count(*)를 구하는 것이지만 count(*)로 적지 않음.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount(); // 이 부분이 count(*) 임
        db.close();
        return count;
    }

    // CPU 가 움직이는 곳은 여기가 아니라 Activity 이므로 MainActivity 에서 이 함수들을 실행해보러 간다.
}
