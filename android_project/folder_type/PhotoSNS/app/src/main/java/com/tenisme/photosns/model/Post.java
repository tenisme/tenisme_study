package com.tenisme.photosns.model;

public class Post {
    private int userId;
    private int targetedUserId;
    private int publicOn;
    private String photoUrl;
    private String comments;
    private String createdAt;

    public Post(int userId, int targetedUserId, int publicOn, String photoUrl, String comments, String createdAt) {
        this.userId = userId;
        this.targetedUserId = targetedUserId;
        this.publicOn = publicOn;
        this.photoUrl = photoUrl;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTargetedUserId() {
        return targetedUserId;
    }

    public void setTargetedUserId(int targetedUserId) {
        this.targetedUserId = targetedUserId;
    }

    public int getPublicOn() {
        return publicOn;
    }

    public void setPublicOn(int publicOn) {
        this.publicOn = publicOn;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
