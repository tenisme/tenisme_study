package com.tenisme.searchplace.model;

public class SearchPlace {
    private int id;
    private double lat;
    private double lng;
    private String placeName;
    private String vicinity;
    private int saved;

    public SearchPlace() {
    }

    public SearchPlace(double lat, double lng, String placeName, String vicinity) {
        this.lat = lat;
        this.lng = lng;
        this.placeName = placeName;
        this.vicinity = vicinity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public int getSaved() {
        return saved;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }
}
