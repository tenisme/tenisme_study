package com.tenisme.trackme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tenisme.trackme.model.Activity;
import com.tenisme.trackme.model.ButtonSeat;
import com.tenisme.trackme.model.TimeRecord;
import com.tenisme.trackme.model.Sentence;
import com.tenisme.trackme.util.Utils;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor;
    ContentValues values;

    Sentence sentence;
    TimeRecord timeRecord;
    Activity activity;
    ButtonSeat buttonSeat;

    String log = Utils.LOG;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }

    // create table
    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.i(log, "start create table");

        // sentence 테이블 생성 쿼리
        String CREATE_SENTENCE_TABLE = "create table " + Utils.TABLE_SENTENCE +
                "(" + Utils.KEY_SENTENCE_ID + " integer not null primary key autoincrement, "
                + Utils.KEY_SENTENCE + " text not null unique)";
//        Log.i(log, CREATE_SENTENCE_TABLE);

        // activity 테이블 생성 쿼리
        String CREATE_ACTIVITY_TABLE = "create table " + Utils.TABLE_ACTIVITY +
                "(" + Utils.KEY_ACTIVITY_ID + " integer not null primary key autoincrement, "
                + Utils.KEY_NAME + " text not null, "
                + Utils.KEY_ICON + " integer not null, "
                + Utils.KEY_BG_COLOR + " text not null,"
                + "unique(" + Utils.KEY_NAME + ", " + Utils.KEY_ICON + ", " + Utils.KEY_BG_COLOR + "))";
//        Log.i(log, CREATE_ACTIVITY_TABLE);

        // button_seat 테이블 생성 쿼리
        String CREATE_BUTTON_SEAT_TABLE = "create table " + Utils.TABLE_BUTTON_SEAT +
                "(" + Utils.KEY_BUTTON_SEAT_ID + " integer not null primary key autoincrement, "
                + Utils.KEY_LAYOUT_ID + " integer not null unique, "
                + Utils.KEY_ACTIVITY_ID + " integer unique, "
                + Utils.KEY_TIME_RECORD_ID + " integer "
                + "check(" + Utils.KEY_ACTIVITY_ID + " is not null))";
//        Log.i(log, CREATE_BUTTON_SEAT_TABLE);

        // time_record 테이블 생성 쿼리
        String CREATE_TIME_RECORD_TABLE = "create table " + Utils.TABLE_TIME_RECORD +
                "(" + Utils.KEY_TIME_RECORD_ID + " integer not null primary key autoincrement, "
                + Utils.KEY_ACTIVITY_ID + " integer not null, "
                + Utils.KEY_START_TIME + " integer not null, "
                + Utils.KEY_FINISH_TIME + " integer not null, "
                + Utils.KEY_MEMO_IN_RECORD + " text default '')";
//        Log.i(log, CREATE_TIME_RECORD_TABLE);

        // 테이블 생성 쿼리 실행
        db.execSQL(CREATE_SENTENCE_TABLE);
        db.execSQL(CREATE_ACTIVITY_TABLE);
        db.execSQL(CREATE_BUTTON_SEAT_TABLE);
        db.execSQL(CREATE_TIME_RECORD_TABLE);
        Log.i(log, "success create table");
    }

    // drop & recreate table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.i(log, "start recreate table");

        // 테이블 삭제
        dropTable(Utils.TABLE_SENTENCE);
        dropTable(Utils.TABLE_ACTIVITY);
        dropTable(Utils.TABLE_BUTTON_SEAT);
        dropTable(Utils.TABLE_TIME_RECORD);

        // ★★★필수★★★ 테이블 재생성
        onCreate(db);
        Log.i(log, "success recreate table");
    }

    private void dropTable(String tableName) {
        String DROP_TABLE = "drop table " + tableName;
        Log.i(log, DROP_TABLE);
        db.execSQL(DROP_TABLE);
//        Log.i(log, "success drop table "+tableName);
    }


    //// table : sentence ////


    // insert one sentence
    public void addSentence(Sentence sentence) {
//        Log.i(log, "start insert sentence");

        // DB에 값을 추가하기 위한 values 셋팅
        values = new ContentValues();
        values.put(Utils.KEY_SENTENCE, sentence.getSentence());
//        Log.i(log, "success put in values");

        // DB에 sentence 추가
        db.insert(Utils.TABLE_SENTENCE, null, values);
        Log.i(log, "insert sentence in table");
    }

    // get one sentence
    public Sentence getSentence(int sentence_id) {
//        Log.i(log, "start get sentence");

        // select 쿼리 생성
        cursor = db.query(Utils.TABLE_SENTENCE, new String[]{Utils.KEY_SENTENCE_ID,
                        Utils.KEY_SENTENCE}, Utils.KEY_SENTENCE_ID + " = ?",
                new String[]{String.valueOf(sentence_id)},
                null, null, null);
//        Log.i(log, "success query(select)");

        // select 된 데이터가 null 이 아니면 첫 번째 row 로 이동
        if (cursor != null) {
//            Log.i(log, "run if(cursor != null)");
            cursor.moveToFirst();
//            Log.i(log, "success cursor.moveToFirst()");
        }

        // 찾아낸 (첫 번째) 데이터에서 컬럼 값들을 각각 뽑아냄
        int selectedSentenceId = Integer.parseInt(cursor.getString(0));
        String selectedSentence = cursor.getString(1);
//        Log.i(log, "selectedSentenceId : " + selectedSentenceId);
//        Log.i(log, "selectedSentence : " + selectedSentence);

        // 뽑아낸 각 컬럼 값들을 sentence 에 저장
        sentence = new Sentence(selectedSentenceId, selectedSentence);
        Log.i(log, "success setting data on 'sentence'");

        // 저장한 sentence 를 return
        return sentence;
    }

    // get all sentence
    public ArrayList<Sentence> getAllSentences() {
//        Log.i(log, "start get all sentence");

        // 어레이리스트를 리턴하기 위해 ArrayList<Sentence> 생성
        ArrayList<Sentence> sentenceArrayList = new ArrayList<>();

        // select all query 생성
        String selectAll = "select * from " + Utils.TABLE_SENTENCE;
//        Log.i(log, selectAll);
        // select all query 실행
        cursor = db.rawQuery(selectAll, null);
//        Log.i(log, "success query(select)");

        // cursor 의 값들을 어레이리스트에 옮겨담기
        if (cursor.moveToFirst()) {
//            Log.i(log, "run if(cursor.moveToFirst())");
            do {
                int selectedSentenceId = Integer.parseInt(cursor.getString(0));
                String selectedSentence = cursor.getString(1);
//                Log.i(log, "selectedSentenceId : " + selectedSentenceId);
//                Log.i(log, "selectedSentence : " + selectedSentence);

                sentence = new Sentence(selectedSentenceId, selectedSentence);

                sentenceArrayList.add(sentence);
//                Log.i(log, "add sentence on sentenceArrayList");
            } while (cursor.moveToNext());
        }
        Log.i(log, "success add on sentenceArrayList");

        return sentenceArrayList;
    }

    // update one sentence
    public void updateSentence(Sentence sentence) {
//        Log.i(log, "start update sentence");

        // DB에 값을 추가하기 위한 values 셋팅
        values = new ContentValues();
        values.put(Utils.KEY_SENTENCE, sentence.getSentence());
//        Log.i(log, "success put in values");

        // update sentence
        db.update(Utils.TABLE_SENTENCE, values, Utils.KEY_SENTENCE_ID + " = ?",
                new String[]{String.valueOf(sentence.getSentence_id())});
        Log.i(log, "success update sentence");
    }

    // delete one sentence
    public void deleteSentence(Sentence sentence) {
//        Log.i(log, "start delete sentence");

        // delete sentence
        db.delete(Utils.TABLE_SENTENCE, Utils.KEY_SENTENCE_ID + " = ?",
                new String[]{String.valueOf(sentence.getSentence_id())});
        Log.i(log, "success delete sentence");
    }

    // get the total count of TABLE_SENTENCE
    public int getSentencesCount() {
        Log.i(log, "start get the total count of TABLE_SENTENCE");

        // get count
        String countQuery = "select * from " + Utils.TABLE_SENTENCE;
        cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount(); // count(*)
    }


    //// table : activity ////


    // add one activity
    public void addActivity(Activity activity) {
//        Log.i(log, "start insert activity");

        // DB에 값을 추가하기 위한 values 셋팅
        values = new ContentValues();
        values.put(Utils.KEY_NAME, activity.getActivity_name());
        values.put(Utils.KEY_ICON, activity.getActivity_icon());
        values.put(Utils.KEY_BG_COLOR, activity.getActivity_bg_color());
//        Log.i(log, "success put in values");

        // DB에 sentence 추가
        db.insert(Utils.TABLE_ACTIVITY, null, values);
//        Log.i(log, "insert activity_id : " + getActivityId);
    }

    // get one activity
    public Activity getActivity(int activity_id) {
//        Log.i(log, "start get activity");

        // select 쿼리 생성
        cursor = db.query(Utils.TABLE_ACTIVITY, new String[]{Utils.KEY_ACTIVITY_ID,
                        Utils.KEY_NAME, Utils.KEY_ICON, Utils.KEY_BG_COLOR}, Utils.KEY_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(activity_id)},
                null, null, null);
//        Log.i(log, "success query(select)");

        // select 된 데이터가 null 이 아니면 첫 번째 row 로 이동
        if (cursor != null) {
//            Log.i(log, "run if(cursor != null)");
            cursor.moveToFirst();
//            Log.i(log, "success cursor.moveToFirst()");
        }

        // 찾아낸 (첫 번째) 데이터에서 컬럼 값들을 각각 뽑아냄
        int selectedActivityId = Integer.parseInt(cursor.getString(0));
        String selectedActivityName = cursor.getString(1);
        int selectedActivityIcon = Integer.parseInt(cursor.getString(2));
        String selectedBGColor = cursor.getString(3);

        activity = new Activity(selectedActivityId, selectedActivityName,
                selectedActivityIcon, selectedBGColor);
//        Log.i(log, "success setting data on activity");

        // 저장한 activity 를 return
        return activity;
    }

    // get all activities
    public ArrayList<Activity> getAllActivities() {
//        Log.i(log, "start get all activities");

        // 어레이리스트를 리턴하기 위해 ArrayList<Activity> 생성
        ArrayList<Activity> activityArrayList = new ArrayList<>();

        // select all query 생성
        String selectAll = "select * from " + Utils.TABLE_ACTIVITY;
//        Log.i(log, selectAll);
        // select all query 실행
        cursor = db.rawQuery(selectAll, null);
//        Log.i(log, "success query(select)");

        // cursor 의 값들을 어레이리스트에 옮겨담기
        if (cursor.moveToFirst()) {
//            Log.i(log, "run if(cursor.moveToFirst())");
            do {
                int selectedActivityId = Integer.parseInt(cursor.getString(0));
                String selectedActivityName = cursor.getString(1);
                int selectedActivityIcon = Integer.parseInt(cursor.getString(2));
                String selectedBGColor = cursor.getString(3);

                activity = new Activity(selectedActivityId, selectedActivityName,
                        selectedActivityIcon, selectedBGColor);

                activityArrayList.add(activity);
//                Log.i(log, "add record time on activityArrayList");
            } while (cursor.moveToNext());
        }
//        Log.i(log, "success add on activityArrayList");

        return activityArrayList;
    }

    // update one activity
    public void updateActivity(Activity activity) {
//        Log.i(log, "start update activity");

        // DB에 값을 추가하기 위한 values 셋팅
        values = new ContentValues();
        values.put(Utils.KEY_NAME, activity.getActivity_name());
        values.put(Utils.KEY_ICON, activity.getActivity_icon());
        values.put(Utils.KEY_BG_COLOR, activity.getActivity_bg_color());
//        Log.i(log, "success put in values");

        // update sentence
        db.update(Utils.TABLE_ACTIVITY, values, Utils.KEY_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(activity.getActivity_id())});
        Log.i(log, "success update activity");
    }

    // delete one activity
    public void deleteActivity(Activity activity) {
//        Log.i(log, "start delete activity");

        // delete activity
        db.delete(Utils.TABLE_ACTIVITY, Utils.KEY_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(activity.getActivity_id())});
        Log.i(log, "success delete activity");
    }

    // get the total count of TABLE_ACTIVITY
    public int getActivitiesCount() {
        Log.i(log, "start get the total count of TABLE_ACTIVITY");

        // get count
        String countQuery = "select * from " + Utils.TABLE_ACTIVITY;
        cursor = db.rawQuery(countQuery, null);

        return cursor.getCount(); // count(*)
    }


    //// table : button_seat ////


    // add one seat
    public long addButtonSeat(ButtonSeat buttonSeat) {

        values = new ContentValues();
        values.put(Utils.KEY_LAYOUT_ID, buttonSeat.getLayout_id());
        values.put(Utils.KEY_ACTIVITY_ID, buttonSeat.getActivity_id());
        // addButtonSeat 는 자리 셋팅을 위한 함수이므로 시간 기록(time_record_id)을 같이 저장하지 않음.

        return db.insert(Utils.TABLE_BUTTON_SEAT, null, values);
    }

    // get one seat by button_seat_id
    public ButtonSeat getButtonSeatById(int button_seat_id) {
        Log.i(log, "start get button_seat by button_seat_id");

        // select 쿼리 생성
        cursor = db.query(Utils.TABLE_BUTTON_SEAT,
                new String[]{Utils.KEY_BUTTON_SEAT_ID, Utils.KEY_LAYOUT_ID,
                        Utils.KEY_ACTIVITY_ID, Utils.KEY_TIME_RECORD_ID},
                Utils.KEY_BUTTON_SEAT_ID + " = ?",
                new String[]{String.valueOf(button_seat_id)},
                null, null, null);
//        Log.i(log, "success query(select)");

        // select 된 데이터가 null 이 아니면 첫 번째 row 로 이동
        if (cursor != null) {
//            Log.i(log, "run if(cursor != null)");
            cursor.moveToFirst();
//            Log.i(log, "success cursor.moveToFirst()");
        }

        if(cursor.getString(2) == null){
            int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
            int selectedLayoutId = Integer.parseInt(cursor.getString(1));

            buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId);
        } else {
            if(cursor.getString(3) == null){
                int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                int selectedLayoutId = Integer.parseInt(cursor.getString(1));
                int selectedActivityId = Integer.parseInt(cursor.getString(2));

                buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId,
                        selectedActivityId);
            } else {
                int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                int selectedLayoutId = Integer.parseInt(cursor.getString(1));
                int selectedActivityId = Integer.parseInt(cursor.getString(2));
                int selectedTimeRecordId = Integer.parseInt(cursor.getString(3));

                buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId,
                        selectedActivityId, selectedTimeRecordId);
            }
        }

        // 저장한 sentence 를 return
        return buttonSeat;
    }

    // get one seat by layout_id
    public ButtonSeat getButtonSeatByLayoutId(int layout_id) {
        Log.i(log, "start get button_seat by layout_id");

        // select 쿼리 생성
        cursor = db.query(Utils.TABLE_BUTTON_SEAT,
                new String[]{Utils.KEY_BUTTON_SEAT_ID, Utils.KEY_LAYOUT_ID,
                        Utils.KEY_ACTIVITY_ID, Utils.KEY_TIME_RECORD_ID},
                Utils.KEY_LAYOUT_ID + " = ?",
                new String[]{String.valueOf(layout_id)},
                null, null, null);
//        Log.i(log, "success query(select)");

        // select 된 데이터가 null 이 아니면 첫 번째 row 로 이동
        if (cursor != null) {
//            Log.i(log, "run if(cursor != null)");
            cursor.moveToFirst();
//            Log.i(log, "success cursor.moveToFirst()");
        }

        if(cursor.getString(2) == null){
            int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
            int selectedLayoutId = Integer.parseInt(cursor.getString(1));

            buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId);
        } else {
            if(cursor.getString(3) == null){
                int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                int selectedLayoutId = Integer.parseInt(cursor.getString(1));
                int selectedActivityId = Integer.parseInt(cursor.getString(2));

                buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId,
                        selectedActivityId);
            } else {
                int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                int selectedLayoutId = Integer.parseInt(cursor.getString(1));
                int selectedActivityId = Integer.parseInt(cursor.getString(2));
                int selectedTimeRecordId = Integer.parseInt(cursor.getString(3));

                buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId,
                        selectedActivityId, selectedTimeRecordId);
            }
        }
        return buttonSeat;
    }

    // get all seats
    public ArrayList<ButtonSeat> getAllButtonSeat() {
//        Log.i(log, "start get all seats");

        // 어레이리스트를 리턴하기 위해 ArrayList 생성
        ArrayList<ButtonSeat> buttonSeatArrayList = new ArrayList<>();

        // select all query 생성
        String selectAll = "select * from " + Utils.TABLE_BUTTON_SEAT;
//        Log.i(log, selectAll);
        // select all query 실행
        cursor = db.rawQuery(selectAll, null);
//        Log.i(log, "success query(select)");

        // cursor 의 값들을 어레이리스트에 옮겨담기
        if (cursor.moveToFirst()) {
//            Log.i(log, "run if(cursor.moveToFirst())");
            do {
                if(cursor.getString(2) == null){
                    int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                    int selectedLayoutId = Integer.parseInt(cursor.getString(1));

                    buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId);
                } else {
                    if(cursor.getString(3) == null){
                        int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                        int selectedLayoutId = Integer.parseInt(cursor.getString(1));
                        int selectedActivityId = Integer.parseInt(cursor.getString(2));

                        buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId,
                                selectedActivityId);
                    } else {
                        int selectedButtonSeatId = Integer.parseInt(cursor.getString(0));
                        int selectedLayoutId = Integer.parseInt(cursor.getString(1));
                        int selectedActivityId = Integer.parseInt(cursor.getString(2));
                        int selectedTimeRecordId = Integer.parseInt(cursor.getString(3));

                        buttonSeat = new ButtonSeat(selectedButtonSeatId, selectedLayoutId,
                                selectedActivityId, selectedTimeRecordId);
                    }
                }
                buttonSeatArrayList.add(buttonSeat);
//                Log.i(log, "add sentence on buttonSeatArrayList");
            } while (cursor.moveToNext());
        }
        Log.i(log, "success add on buttonSeatArrayList");

        return buttonSeatArrayList;
    }

    // update one seat
    public void updateButtonSeat(ButtonSeat buttonSeat) {
//        Log.i(log, "start update button_seat");

        values = new ContentValues(); // layout_id는 고정이기 때문에 update 하지 않음
        if(buttonSeat.getActivity_id() == null) {
            // 버튼 자리에 활동이 배치되어있지 않을 때 : 유저가 버튼 셋팅을 수정할 때 사용
            values.put(Utils.KEY_ACTIVITY_ID, buttonSeat.getActivity_id());
        }else{
            // 버튼 자리에 활동이 배치되어 있을 때 : 시간 기록 종료시 time_record_id를 변경하는 데에 사용
            values.put(Utils.KEY_ACTIVITY_ID, buttonSeat.getActivity_id());
            values.put(Utils.KEY_TIME_RECORD_ID, buttonSeat.getTime_record_id());
        }

//        Log.i(log, "success put in values");

        // update sentence
        db.update(Utils.TABLE_BUTTON_SEAT, values, Utils.KEY_BUTTON_SEAT_ID + " = ?",
                new String[]{String.valueOf(buttonSeat.getButton_seat_id())});
        Log.i(log, "success update button_seat");
    }

    // delete one seat
    public void deleteButtonSeat(ButtonSeat buttonSeat) {
//        Log.i(log, "start delete button_seat");

        // delete button_seat
        db.delete(Utils.TABLE_BUTTON_SEAT, Utils.KEY_BUTTON_SEAT_ID + " = ?",
                new String[]{String.valueOf(buttonSeat.getButton_seat_id())});
        Log.i(log, "success delete button_seat");
    }

    // get the total count of TABLE_BUTTON_SEAT
    public int getButtonSeatsCount() {
        Log.i(log, "start get the total count of TABLE_BUTTON_SEAT");

        String countQuery = "select * from " + Utils.TABLE_BUTTON_SEAT;
        cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount(); // count(*)
    }


    //// table : time_record ////


    // INSERT TIME at start or UPDATE TIME at finish
    public long addTimeRecord(TimeRecord timeRecord, boolean isRecording) {

        // isRecording : true -> 기록 시작(기록중), false -> 기록 종료

        if (isRecording) {
            Log.i(log, "Start recording activity");

            // 기록하는 활동, 시작/종료 시간을 values 에 추가 (시작, 종료 시간을 똑같은 값으로 insert)
            values = new ContentValues();
            values.put(Utils.KEY_ACTIVITY_ID, timeRecord.getActivity_id());
            values.put(Utils.KEY_START_TIME, timeRecord.getStart_time());
            values.put(Utils.KEY_FINISH_TIME, timeRecord.getFinish_time());

            // insert start time at table
            return db.insert(Utils.TABLE_TIME_RECORD, null, values);
        } else {
            Log.i(log, "Finish recording activity");

            // 기록 종료 시간을 values 에 추가
            values = new ContentValues();
            values.put(Utils.KEY_FINISH_TIME, timeRecord.getFinish_time());
//            Log.i(log, "success put in values");

            // update finish time at table
            db.update(Utils.TABLE_TIME_RECORD, values, Utils.KEY_TIME_RECORD_ID + " = ?",
                    new String[]{String.valueOf(timeRecord.getTime_record_id())});

            return 0L;
        }
    }

    // update MEMO in record
    public void updateMemoInRecord(TimeRecord timeRecord) {

        values = new ContentValues();
        values.put(Utils.KEY_MEMO_IN_RECORD, timeRecord.getMemo_in_record());

        db.update(Utils.TABLE_TIME_RECORD, values, Utils.KEY_TIME_RECORD_ID + " = ?",
                new String[]{String.valueOf(timeRecord.getTime_record_id())});
    }

    // get one TimeRecord
    public TimeRecord getTimeRecord(int time_record_id) {
//        Log.i(log, "start get time record");

        // select 쿼리 생성
        cursor = db.query(Utils.TABLE_TIME_RECORD, new String[]{Utils.KEY_TIME_RECORD_ID,
                        Utils.KEY_ACTIVITY_ID, Utils.KEY_START_TIME, Utils.KEY_FINISH_TIME,
                        Utils.KEY_MEMO_IN_RECORD}, Utils.KEY_TIME_RECORD_ID + " = ?",
                new String[]{String.valueOf(time_record_id)},
                null, null, null);
//        Log.i(log, "success query(select)");

        // select 된 데이터가 null 이 아니면 첫 번째 row 로 이동
        if (cursor != null) {
//            Log.i(log, "run if(cursor != null)");
            cursor.moveToFirst();
//            Log.i(log, "success cursor.moveToFirst()");
        }

        // 찾아낸 (첫 번째) 데이터에서 컬럼 값들을 각각 뽑아냄
        int selectedTimeRecordId = Integer.parseInt(cursor.getString(0));
        int selectedActivityId = Integer.parseInt(cursor.getString(1));
        long selectedStartTime = Long.parseLong(cursor.getString(2));
        long selectedFinishTime = Long.parseLong(cursor.getString(3));
        String selectedMemoInRecord = cursor.getString(4);
//        Log.i(log, "selectedRecordId : " + selectedRecordId);

        // 뽑아낸 각 컬럼 값들을 sentence 에 저장
        timeRecord = new TimeRecord(selectedTimeRecordId, selectedActivityId, selectedStartTime,
                selectedFinishTime, selectedMemoInRecord);
//        Log.i(log, "success setting data on record time");

        // 저장한 sentence 를 return
        return timeRecord;
    }

    // get part of time records
    public ArrayList<TimeRecord> getRecordParts(long startDate, long finishDate) {
        Log.i(log, "start get time record");

        // 어레이리스트를 리턴하기 위해 ArrayList<TimeRecord> 생성
        ArrayList<TimeRecord> timeRecordArrayList = new ArrayList<>();

        // select 쿼리 생성
        cursor = db.query(Utils.TABLE_TIME_RECORD, new String[]{Utils.KEY_TIME_RECORD_ID,
                        Utils.KEY_ACTIVITY_ID, Utils.KEY_START_TIME, Utils.KEY_FINISH_TIME,
                        Utils.KEY_MEMO_IN_RECORD},
                Utils.KEY_START_TIME + " <= ? and " + Utils.KEY_FINISH_TIME + " >= ?",
                new String[]{String.valueOf(startDate), String.valueOf(finishDate)},
                null, null, null);
//        Log.i(log, "success query(select)");

        // select 된 데이터가 null 이 아니면 첫 번째 row 로 이동
        if (cursor != null) {
//            Log.i(log, "run if(cursor != null)");
            cursor.moveToFirst();
//            Log.i(log, "success cursor.moveToFirst()");
        }

        // cursor 의 값들을 어레이리스트에 옮겨담기
        if (cursor.moveToFirst()) {
            do {
                int selectedTimeRecordId = Integer.parseInt(cursor.getString(0));
                int selectedActivityId = Integer.parseInt(cursor.getString(1));
                long selectedStartTime = Long.parseLong(cursor.getString(2));
                long selectedFinishTime = Long.parseLong(cursor.getString(3));
                String selectedMemoInRecord = cursor.getString(4);

                // 뽑아낸 각 컬럼 값들을 timeRecord 에 저장
                timeRecord = new TimeRecord(selectedTimeRecordId, selectedActivityId,
                        selectedStartTime, selectedFinishTime, selectedMemoInRecord);

                timeRecordArrayList.add(timeRecord);
            } while (cursor.moveToNext());
        }

        return timeRecordArrayList;
    }

    // get all record time
    public ArrayList<TimeRecord> getAllRecords() {
//        Log.i(log, "start get all records");

        // 어레이리스트를 리턴하기 위해 ArrayList<TimeRecord> 생성
        ArrayList<TimeRecord> timeRecordArrayList = new ArrayList<>();

        // select all query 생성
        String selectAll = "select * from " + Utils.TABLE_TIME_RECORD;
//        Log.i(log, selectAll);
        // select all query 실행
        cursor = db.rawQuery(selectAll, null);
//        Log.i(log, "success query(select)");

        // cursor 의 값들을 어레이리스트에 옮겨담기
        if (cursor.moveToFirst()) {
//            Log.i(log, "run if(cursor.moveToFirst())");
            do {
                int selectedTimeRecordId = Integer.parseInt(cursor.getString(0));
                int selectedActivityId = Integer.parseInt(cursor.getString(1));
                long selectedStartTime = Long.parseLong(cursor.getString(2));
                long selectedFinishTime = Long.parseLong(cursor.getString(3));
                String selectedMemoInRecord = cursor.getString(4);

                // 뽑아낸 각 컬럼 값들을 timeRecord 에 저장
                timeRecord = new TimeRecord(selectedTimeRecordId, selectedActivityId,
                        selectedStartTime, selectedFinishTime, selectedMemoInRecord);

                timeRecordArrayList.add(timeRecord);
//                Log.i(log, "add record time on timeRecordArrayList");
            } while (cursor.moveToNext());
        }
//        Log.i(log, "success add on timeRecordArrayList");

        return timeRecordArrayList;
    }

    // update one record time : 기록 수정 화면에서 사용
    public void updateRecordTime(TimeRecord timeRecord) {
//        Log.i(log, "start update TimeRecord");

        // DB에 값을 추가하기 위한 values 셋팅
        values = new ContentValues();
        values.put(Utils.KEY_ACTIVITY_ID, timeRecord.getActivity_id());
        values.put(Utils.KEY_START_TIME, timeRecord.getStart_time());
        values.put(Utils.KEY_FINISH_TIME, timeRecord.getFinish_time());
        values.put(Utils.KEY_MEMO_IN_RECORD, timeRecord.getMemo_in_record());
//            Log.i(log, "success put in values");

        // update time
        db.update(Utils.TABLE_TIME_RECORD, values, Utils.KEY_TIME_RECORD_ID + " = ?",
                new String[]{String.valueOf(timeRecord.getTime_record_id())});
        Log.i(log, "success update finish time");
    }

    // delete one time record
    public void deleteRecordTime(TimeRecord timeRecord) {
//        Log.i(log, "start delete TimeRecord");

        // delete sentence
        db.delete(Utils.TABLE_TIME_RECORD, Utils.KEY_TIME_RECORD_ID + " = ?",
                new String[]{String.valueOf(timeRecord.getTime_record_id())});
        Log.i(log, "success delete time record");
    }

    // get the total count of TABLE_RECORD_TIME
    public int getRecordsCount() {
        Log.i(log, "start get the total count of TABLE_RECORD_TIME");

        // get count
        String countQuery = "select * from " + Utils.TABLE_TIME_RECORD;
        cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount(); // count(*)
    }

}
