package com.tenisme.trackme.model;

public class ButtonSeat {

    private int button_seat_id;
    private int layout_id;
    private Integer activity_id;
    private Integer time_record_id;

    public ButtonSeat() {
    }

    public ButtonSeat(int button_seat_id, int layout_id) {
        this.button_seat_id = button_seat_id;
        this.layout_id = layout_id;
    }

    public ButtonSeat(int button_seat_id, int layout_id, Integer activity_id) {
        this.button_seat_id = button_seat_id;
        this.layout_id = layout_id;
        this.activity_id = activity_id;
    }

    public ButtonSeat(int button_seat_id, int layout_id, Integer activity_id, Integer time_record_id) {
        this.button_seat_id = button_seat_id;
        this.layout_id = layout_id;
        this.activity_id = activity_id;
        this.time_record_id = time_record_id;
    }

    public int getButton_seat_id() {
        return button_seat_id;
    }

    public void setButton_seat_id(int button_seat_id) {
        this.button_seat_id = button_seat_id;
    }

    public int getLayout_id() {
        return layout_id;
    }

    public void setLayout_id(int layout_id) {
        this.layout_id = layout_id;
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public Integer getTime_record_id() {
        return time_record_id;
    }

    public void setTime_record_id(Integer time_record_id) {
        this.time_record_id = time_record_id;
    }
}
