package com.app.lystn.pojo.artiste;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArtisteDetailPOJO implements Serializable {
    public String getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
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

    public String getImgIrl() {
        return imgIrl;
    }

    public void setImgIrl(String imgIrl) {
        this.imgIrl = imgIrl;
    }

    @SerializedName("podcast_id")
    @Expose
    private String podcastId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imgIrl")
    @Expose
    private String imgIrl;

}
