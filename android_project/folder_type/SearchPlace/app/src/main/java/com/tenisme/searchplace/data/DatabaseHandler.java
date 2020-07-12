package com.tenisme.searchplace.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tenisme.searchplace.model.SearchPlace;
import com.tenisme.searchplace.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;
    ContentValues values;
    Cursor cursor;
    ArrayList<SearchPlace> searchPlaces;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANS_TABLE = "create table " + Util.TABLE_NAME + "(" + Util.KEY_ID
                + " integer not null primary key autoincrement, "
                + Util.KEY_LAT + " double, "
                + Util.KEY_LNG + " double, "
                + Util.KEY_PLACE_NAME + " text, "
                + Util.KEY_ADDRESS + " text, "
                + Util.KEY_SAVED + " integer)";
        db.execSQL(CREATE_TRANS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "drop table " + Util.TABLE_NAME;
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    public void addPlace(SearchPlace searchPlace){
        db = this.getWritableDatabase();
        // 2. db에 저장하기 위해서는 ContentValues 를 이용한다.
        values = new ContentValues();
        values.put(Util.KEY_LAT, searchPlace.getLat());
        values.put(Util.KEY_LNG, searchPlace.getLng());
        values.put(Util.KEY_PLACE_NAME, searchPlace.getPlaceName());
        values.put(Util.KEY_ADDRESS, searchPlace.getVicinity());
        values.put(Util.KEY_SAVED, searchPlace.getSaved());
        // 3. db에 실제로 저장한다.
        db.insert(Util.TABLE_NAME, null, values);
        db.close(); // insert 하면 항상 close 를 해준다.
    }

    public SearchPlace getPlace(int id){
        db = this.getReadableDatabase();
        cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_LAT, Util.KEY_LNG,
                        Util.KEY_PLACE_NAME,Util.KEY_ADDRESS,Util.KEY_SAVED}, Util.KEY_ID + " =?",
                        new String[]{String.valueOf(id)},null, null, null);

        if (cursor != null) {
            cursor.moveToFirst(); // 커서(셀렉트한 것(들))의 첫번째(맨 위 데이터)로 이동해라
        }

        int selectedId = Integer.parseInt(cursor.getString(0));
        double selectedLat = Double.parseDouble(cursor.getString(1));
        double selectedLng = Double.parseDouble(cursor.getString(2));
        String selectedPlaceName = cursor.getString(3);
        String selectedAddress = cursor.getString(4);
        int selectedSaved = Integer.parseInt(cursor.getString(5));

        // DB 에서 읽어온 데이터(위)를 자바 클래스로 처리한다
        SearchPlace searchPlace = new SearchPlace(); // 여기에 정보를 담아준다
        searchPlace.setId(selectedId);
        searchPlace.setLat(selectedLat);
        searchPlace.setLng(selectedLng);
        searchPlace.setPlaceName(selectedPlaceName);
        searchPlace.setVicinity(selectedAddress);
        searchPlace.setSaved(selectedSaved);

        return searchPlace;
    }

    public ArrayList<SearchPlace> getAllPlace() {
        searchPlaces = new ArrayList<>();

        String selectAll = "select * from " + Util.TABLE_NAME + " order by " + Util.KEY_ID + " desc";

        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectAll, null); // 여기의 null > where 가 필요없다는 뜻

        if (cursor.moveToFirst()) {
            do {
                int selectedId = Integer.parseInt(cursor.getString(0));
                double selectedLat = Double.parseDouble(cursor.getString(1));
                double selectedLng = Double.parseDouble(cursor.getString(2));
                String selectedPlaceName = cursor.getString(3);
                String selectedAddress = cursor.getString(4);
                int selectedSaved = Integer.parseInt(cursor.getString(5));

                SearchPlace searchPlace = new SearchPlace();
                searchPlace.setId(selectedId);
                searchPlace.setLat(selectedLat);
                searchPlace.setLng(selectedLng);
                searchPlace.setPlaceName(selectedPlaceName);
                searchPlace.setVicinity(selectedAddress);
                searchPlace.setSaved(selectedSaved);

                searchPlaces.add(searchPlace);

            } while (cursor.moveToNext());
        }
        return searchPlaces;
    }

    public void deletePlace(SearchPlace searchPlace){
        db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID+" = ?", new String[]{String.valueOf(searchPlace.getId())});
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
