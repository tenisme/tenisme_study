package com.tenisme.movieapp.model;

import java.util.Date;

public class Movie {
    private int movie_id;
    private String title;
    private String genre;
    private int attendance;
    private String year;
    private int is_favorite;

    public Movie() {
    }

    public Movie(int movie_id, String title, String genre, int attendance, String year, int is_favorite) {
        this.movie_id = movie_id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
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

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }
}
