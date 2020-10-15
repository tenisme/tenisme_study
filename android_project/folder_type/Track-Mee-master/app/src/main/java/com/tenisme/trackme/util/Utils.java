package com.tenisme.trackme.util;

import com.tenisme.trackme.R;

public class Utils {

    // 로그
    public static final String LOG = "trackMeLog";

    // SharedPreferences
    public static final String PREFERENCES_NAME = "track_me";

    // DB 이름 및 버전 지정
    public static final String DATABASE_NAME = "track_me_db";
    public static final int DATABASE_VERSION = 1;

    // 테이블 이름 지정
    public static final String TABLE_SENTENCE = "sentence";
    public static final String TABLE_ACTIVITY = "activity";
    public static final String TABLE_BUTTON_SEAT = "button_seat";
    public static final String TABLE_TIME_RECORD = "time_record";
    public static final String TABLE_TAG = "tag";
    public static final String TABLE_ACTIVITY_TAG = "activity_tag";
    public static final String TABLE_TIME_RECORD_TAG = "time_record_tag";
    public static final String TABLE_DAILY_RECORD = "daily_record";

    // sentence 테이블 컬럼 이름 지정
    public static final String KEY_SENTENCE_ID = "sentence_id";
    public static final String KEY_SENTENCE = "sentence";

    // activity 테이블 컬럼 이름 지정
    public static final String KEY_ACTIVITY_ID = "activity_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ICON = "icon";
    public static final String KEY_BG_COLOR = "bg_color";

    // button_seat 테이블 컬럼 이름 지정
    public static final String KEY_BUTTON_SEAT_ID = "button_seat_id";
    public static final String KEY_LAYOUT_ID = "layout_id";
    // + KEY_ACTIVITY_ID
    // + KEY_TIME_RECORD_ID

    // time_record 테이블 컬럼 이름 지정
    public static final String KEY_TIME_RECORD_ID = "time_record_id";
    // + KEY_ACTIVITY_ID
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_FINISH_TIME = "finish_time";
    public static final String KEY_MEMO_IN_RECORD = "memo_in_record";

    // tag 테이블 컬럼 이름 지정
    public static final String KEY_TAG_ID = "tag_id";
    public static final String KEY_TAG = "tag";

    // activity_tag 테이블 컬럼 이름 지정
    public static final String KEY_ACTIVITY_TAG_ID = "activity_tag_id";
    // + KEY_ACTIVITY_ID
    // + KEY_TAG_ID

    // time_record_tag 테이블 컬럼 이름 지정
    public static final String KEY_TIME_RECORD_TAG_ID = "time_record_tag_id";
    // + KEY_TIME_RECORD_ID
    // + KEY_TAG_ID

    // daily_record 테이블 컬럼 이름 지정
    public static final String KEY_DAILY_RECORD_ID = "daily_record_id";
    public static final String KEY_DAY = "day";
    public static final String KEY_FEELING_ICON = "feeling_icon";
    public static final String KEY_DAILY_NOTE = "daily_note";

}
