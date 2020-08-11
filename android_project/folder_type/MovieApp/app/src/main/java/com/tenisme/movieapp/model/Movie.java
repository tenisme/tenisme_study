package com.tenisme.movieapp.model;

import java.util.Date;

public class Movie {
    private int movie_id;
    private String title;
    private String genre;
    private int attendance;
    private String year;
    private int cnt_comments;
    private Double avg_rating;
    private int is_favorite;

    public Movie() {
    }

    public Movie(int movie_id, String title, String genre, int attendance, String year, int cnt_comments, Double avg_rating, int is_favorite) {
        this.movie_id = movie_id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.cnt_comments = cnt_comments;
        this.avg_rating = avg_rating;
        this.is_favorite = is_favorite;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getCnt_comments() {
        return cnt_comments;
    }

    public void setCnt_comments(int cnt_comments) {
        this.cnt_comments = cnt_comments;
    }

    public Double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(Double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }
}
