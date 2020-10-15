package com.tenisme.trackme.model;

public class Activity {

    private int activity_id;
    private String activity_name;
    private int activity_icon;
    private String activity_bg_color;

    public Activity() {
    }

    public Activity(int activity_id, String activity_name, int activity_icon, String activity_bg_color) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.activity_icon = activity_icon;
        this.activity_bg_color = activity_bg_color;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getActivity_icon() {
        return activity_icon;
    }

    public void setActivity_icon(int activity_icon) {
        this.activity_icon = activity_icon;
    }

    public String getActivity_bg_color() {
        return activity_bg_color;
    }

    public void setActivity_bg_color(String activity_bg_color) {
        this.activity_bg_color = activity_bg_color;
    }
}
