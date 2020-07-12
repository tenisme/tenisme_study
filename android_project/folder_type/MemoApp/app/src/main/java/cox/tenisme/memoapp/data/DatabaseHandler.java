package cox.tenisme.memoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import cox.tenisme.memoapp.model.Memo;
import cox.tenisme.memoapp.util.Util;

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public DatabaseHandler(@Nullable Context context) {

        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    // 테이블 작성
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACT_TABLE = "create table " + Util.TABLE_NAME + "(" + Util.KEY_ID
                + " integer not null primary key autoincrement, "
                + Util.KEY_TITLE + " text,"
                + Util.KEY_MEMO + " text )";

        db.execSQL(CREATE_CONTACT_TABLE);

    }

    // 테이블 삭제 후 재생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "drop table " + Util.TABLE_NAME;
        db.execSQL(DROP_TABLE);

        onCreate(db);

    }

    // 데이터 추가
    public void addMemo(Memo memo){

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, memo.getTitle());
        values.put(Util.KEY_MEMO, memo.getMemo());

        db.insert(Util.TABLE_NAME, null, values);
        db.close();

    }

    // 데이터 하나씩 리턴
    public Memo getMemo(int id){

        db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_TITLE,
                        Util.KEY_MEMO}, Util.KEY_ID + " =?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int selectedId = Integer.parseInt(cursor.getString(0));

        String selectedTitle = cursor.getString(1);
        String selectedMemo = cursor.getString(2);

        Memo memo = new Memo();
        memo.setId(selectedId);
        memo.setTitle(selectedTitle);
        memo.setMemo(selectedMemo);

        return memo;

    }

    // 모든 데이터 가져오기
    public ArrayList<Memo> getAllMemos(){

        ArrayList<Memo> memoArrayList = new ArrayList<>();

        String selectAll = "select * from " + Util.TABLE_NAME;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {

                int selectedId = Integer.parseInt(cursor.getString(0));

                String selectedTitle = cursor.getString(1);
                String selectedMemo = cursor.getString(2);

                Memo memo = new Memo();
                memo.setId(selectedId);
                memo.setTitle(selectedTitle);
                memo.setMemo(selectedMemo);

                memoArrayList.add(memo);

            } while (cursor.moveToNext());
        }
        return memoArrayList;

    }

    // 데이터 업데이트
    public int updateMemo(Memo memo){

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, memo.getTitle());
        values.put(Util.KEY_MEMO, memo.getMemo());

        int ret = db.update(Util.TABLE_NAME, values, Util.KEY_ID+" = ?", new String[]{String.valueOf(memo.getId())});

        db.close();

        return ret;

    }

    // 데이터 지우기
    public void deleteMemo(Memo memo){

        db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME, Util.KEY_ID+" = ?", new String[]{String.valueOf(memo.getId())});

        db.close();

    }

    // 데이터 개수 구하기
    public int getCount(){

        String countQuery = "select * from "+Util.TABLE_NAME;

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        db.close();

        return count;

    }

    // 데이터 검색하기
    public ArrayList<Memo> searchMemo(String search){

        ArrayList<Memo> memoArrayList = new ArrayList<>();

        String searchMemo = "select * from " + Util.TABLE_NAME + " where "+Util.KEY_TITLE + " like "
                + "\"%" + search + "%\""+ " or " + Util.KEY_MEMO + " like " + "\"%" + search + "%\"";
        // teacher ver.
        // String searchQuery = "select id, title, memo from "+Util.TABLE_NAME+" where "+Util.KEY_TITLE+" like ? or "+Util.KEY_MEMO+" like ?";
            // or 대신에 ||도 가능한가? || 안된다. or로 써야한다.
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(searchMemo, null);
            // teacher ver.
            // Cursor cursor = db.rawQuery(searchQuery, new String[]{"%"+search+"%", "%"+search+"%"});
                // db.rawQuery(searchQuery, new String[]{"%"+search+"%", "%"+search+"%"});
                    // searchQuery 뒤에 적힌 조건문은 searchQuery 값의 맨 뒤에 있던 like ?의 ?를 대체한다.
                    // 쿼리문에 ?가 2개가 들어있으면 new String[]{여기} 안에도 콤마로 구분해서 두 개로 나눠 써야한다.
                    // new String[]{여기} 안에 String 쿼리문의 where 문을 전부 문자열 처리해서 적어놔도 된다..고 한다. 문법이 근데 다르다고 했나.
                    // SQLite 의 문법은 "%A%"가 아니라 %A%라고 한다. 다만 내가 만들어놓은 조건문에는 ""가 들어가있는데도 잘 돌아간다.
                // rawQuery() 안에 select 문과 조건문을 추가함으로서, 변수 cursor 는 where 조건을 가진 select 문 값을 가지게 된다.

        if (cursor.moveToFirst()) {
            do {

                int selectedId = Integer.parseInt(cursor.getString(0));

                String selectedTitle = cursor.getString(1);
                String selectedMemo = cursor.getString(2);

                Memo memo = new Memo();
                memo.setId(selectedId);
                memo.setTitle(selectedTitle);
                memo.setMemo(selectedMemo);

                memoArrayList.add(memo);

            } while (cursor.moveToNext()); // 그 다음 커서를 가져와서 위의 do{}를 다시 실행해라.
        }
        return memoArrayList;

    }
}
