package com.tenisme.papago.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tenisme.papago.model.Translations;
import com.tenisme.papago.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;
    ContentValues values;
    Cursor cursor;
    ArrayList<Translations> translationsArrayList;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANS_TABLE = "create table " + Util.TABLE_NAME + "(" + Util.KEY_ID
                + " integer not null primary key autoincrement, "
                + Util.KEY_TRANS + " text, "
                + Util.KEY_BEFORE + " text,"
                + Util.KEY_AFTER + " text )";
        db.execSQL(CREATE_TRANS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "drop table " + Util.TABLE_NAME;
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    public void addTrans(Translations translations){
        db = this.getWritableDatabase();
        // 2. db에 저장하기 위해서는 ContentValues 를 이용한다.
        values = new ContentValues();
        values.put(Util.KEY_TRANS, translations.getTrans());
        values.put(Util.KEY_BEFORE, translations.getBefore());
        values.put(Util.KEY_AFTER, translations.getAfter());
        // 3. db에 실제로 저장한다.
        db.insert(Util.TABLE_NAME, null, values);
        db.close(); // insert 하면 항상 close 를 해준다.
    }

    public Translations getTrans(int id){
        db = this.getReadableDatabase();
        cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_TRANS, Util.KEY_BEFORE,
                        Util.KEY_AFTER}, Util.KEY_ID + " =?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst(); // 커서(셀렉트한 것(들))의 첫번째(맨 위 데이터)로 이동해라
        }

        int selectedId = Integer.parseInt(cursor.getString(0));
        // 커서의 "컬럼 인덱스(여기서는 id가 0, name 이 1, phone_number 가 2이다)"가 0번째인 것을 문자열로 가져와서 int 타입으로 바꿔라
        String selectedTrans = cursor.getString(1);
        String selectedBefore = cursor.getString(2);
        String selectedAfter = cursor.getString(3);

        // DB 에서 읽어온 데이터(위)를 자바 클래스로 처리한다
        Translations translations = new Translations(); // 여기에 정보를 담아준다
        translations.setId(selectedId);
        translations.setTrans(selectedTrans);
        translations.setBefore(selectedBefore);
        translations.setAfter(selectedAfter);

        return translations;
    }

    public ArrayList<Translations> getAllTrans() {
        translationsArrayList = new ArrayList<>();
        // 2. 데이터베이스에서 모든 데이터를 select(조회) 한다
        // "select" 하는 쿼리문을 만든다
        String selectAll = "select * from " + Util.TABLE_NAME + " order by " + Util.KEY_ID + " desc";
            // SQLite 를 사용할 때는 역순 정렬은 이렇게 하는 것이 자바 코드로 레이아웃 정렬 조정하는 것보다 더 처리 속도가 빠르다
        db = this.getReadableDatabase(); // 읽는다 > read
        cursor = db.rawQuery(selectAll, null); // 여기의 null > where 가 필요없다는 뜻
        // 3. 여러 개의 데이터를 루프를 돌면서 Contact 클래스에 정보를 하나씩 담는다
        if (cursor.moveToFirst()) {
            do {
                // db 에서 가져온다
                int selectedId = Integer.parseInt(cursor.getString(0));
                String selectedTrans = cursor.getString(1);
                String selectedBefore = cursor.getString(2);
                String selectedAfter = cursor.getString(3);

                Translations translations = new Translations(); // 새 클래스(주소)를 만들어서 읽어온 db 데이터들을 담는다
                translations.setId(selectedId);
                translations.setTrans(selectedTrans);
                translations.setBefore(selectedBefore);
                translations.setAfter(selectedAfter);

                // 4. 비어있는 ArrayList 에 데이터를 하나씩 추가시킨다.
                translationsArrayList.add(translations);

            } while (cursor.moveToNext()); // 이걸 다음 데이터가 없을 때까지 계속 반복한다
        }
        return translationsArrayList;
    }

    public int updateTrans(Translations translations){
        db = this.getWritableDatabase();

        values = new ContentValues();
        values.put(Util.KEY_TRANS, translations.getTrans());
        values.put(Util.KEY_BEFORE, translations.getBefore());
        values.put(Util.KEY_AFTER, translations.getAfter());

        // 데이터베이스 테이블 업데이트
        // update contacts set name = "", phone = "" where id = ?;
        // db.update(테이블이름, 업데이트할 값들(values), where 절, ?에 들어가는 것)
        int ret = db.update(Util.TABLE_NAME, values, Util.KEY_ID+" = ?", new String[]{String.valueOf(translations.getId())});
        db.close(); // onDestroy()를 대신 쓸 때도 있음?? 여기서는 이게 맞다고 함.
        return ret;
    }

    public void deleteTrans(Translations translations){
        db = this.getWritableDatabase();
        // db.delete(테이블이름, where 절, ?에 들어가는 것)
        db.delete(Util.TABLE_NAME, Util.KEY_ID+" = ?", new String[]{String.valueOf(translations.getId())});
        db.close();
    }

    public int getCount(){
        String countQuery = "select * from "+Util.TABLE_NAME; // count(*)를 구하는 것이지만 count(*)로 적지 않음.
        db = this.getReadableDatabase();
        cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount(); // 이 부분이 count(*) 임
        db.close();
        return count;
    }
}
