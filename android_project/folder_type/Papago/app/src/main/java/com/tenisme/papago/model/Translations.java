package com.tenisme.papago.model;

public class Translations {
    private int id;
    private String trans;
    private String before;
    private String after;

    public Translations() {
    }

    public Translations(String trans, String before, String after) {
        this.trans = trans;
        this.before = before;
        this.after = after;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
