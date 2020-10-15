package com.tenisme.trackme.model;

public class TimeRecord {

    private int time_record_id;
    private int activity_id;
    private long start_time;
    private long finish_time;
    private String memo_in_record;

    public TimeRecord() {
    }

    public TimeRecord(int activity_id, long start_time, long finish_time) {
        this.activity_id = activity_id;
        this.start_time = start_time;
        this.finish_time = finish_time;
    }

    public TimeRecord(int time_record_id, int activity_id, long start_time, long finish_time, String memo_in_record) {
        this.time_record_id = time_record_id;
        this.activity_id = activity_id;
        this.start_time = start_time;
        this.finish_time = finish_time;
        this.memo_in_record = memo_in_record;
    }

    public int getTime_record_id() {
        return time_record_id;
    }

    public void setTime_record_id(int time_record_id) {
        this.time_record_id = time_record_id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(long finish_time) {
        this.finish_time = finish_time;
    }

    public String getMemo_in_record() {
        return memo_in_record;
    }

    public void setMemo_in_record(String memo_in_record) {
        this.memo_in_record = memo_in_record;
    }
}
