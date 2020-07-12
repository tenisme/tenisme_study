package cox.tenisme.youtube.model;

public class Youtube {
    private String videoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String thumbnailBigUrl;

    public Youtube() {
    }

    public Youtube(String videoId, String title, String description, String thumbnailUrl, String thumbnailBigUrl) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailBigUrl = thumbnailBigUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailBigUrl() {
        return thumbnailBigUrl;
    }

    public void setThumbnailBigUrl(String thumbnailBigUrl) {
        this.thumbnailBigUrl = thumbnailBigUrl;
    }
}
