package com.app.lystn.pojo.artiste;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PodcastDetailPOJO implements Serializable {

    @SerializedName("podcast_id")
    @Expose
    private Integer podcastId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("link_uri")
    @Expose
    private String linkUri;
    @SerializedName("episode_count")
    @Expose
    private Integer episodeCount;
    @SerializedName("img_remote_uri")
    @Expose
    private String imgRemoteUri;
    @SerializedName("img_local_uri")
    @Expose
    private String imgLocalUri;
    @SerializedName("img_height")
    @Expose
    private Integer imgHeight;
    @SerializedName("img_width")
    @Expose
    private Integer imgWidth;
    @SerializedName("img_type")
    @Expose
    private String imgType;
    @SerializedName("added_on")
    @Expose
    private String addedOn;

    public Integer getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Integer podcastId) {
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

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLinkUri() {
        return linkUri;
    }

    public void setLinkUri(String linkUri) {
        this.linkUri = linkUri;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getImgRemoteUri() {
        return imgRemoteUri;
    }

    public void setImgRemoteUri(String imgRemoteUri) {
        this.imgRemoteUri = imgRemoteUri;
    }

    public String getImgLocalUri() {
        return imgLocalUri;
    }

    public void setImgLocalUri(String imgLocalUri) {
        this.imgLocalUri = imgLocalUri;
    }

    public Integer getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(Integer imgHeight) {
        this.imgHeight = imgHeight;
    }

    public Integer getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(Integer imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }
}
