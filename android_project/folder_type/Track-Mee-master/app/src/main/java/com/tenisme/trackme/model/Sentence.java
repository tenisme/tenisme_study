package com.tenisme.trackme.model;

public class Sentence {

    private int sentence_id;
    private String sentence;

    public Sentence() {
    }

    public Sentence(String sentence) {
        this.sentence = sentence;
    }

    public Sentence(int sentence_id, String sentence) {
        this.sentence_id = sentence_id;
        this.sentence = sentence;
    }

    public int getSentence_id() {
        return sentence_id;
    }
    public void setSentence_id(int sentence_id) {
        this.sentence_id = sentence_id;
    }

    public String getSentence() {
        return sentence;
    }
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
