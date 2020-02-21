package com.app.lystn.pojo.artiste;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PodcastEpisodeDetailsPOJO implements Serializable {

    @SerializedName("episode_id")
    @Expose
    private String episodeId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("imgid")
    @Expose
    private Integer imgid;
    @SerializedName("episodetype")
    @Expose
    private String episodetype;
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @SerializedName("episode_seq")
    @Expose
    private Integer episodeSeq;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("playback_count")
    @Expose
    private Integer playbackCount;
    @SerializedName("isexplicit")
    @Expose
    private String isexplicit;
    @SerializedName("stream_uri")
    @Expose
    private String streamUri;
    @SerializedName("length")
    @Expose
    private Integer length;
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

    private boolean downloaded;
    private boolean service_downloaded;
    private PodcastPOJO podcastPOJO;


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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getImgid() {
        return imgid;
    }

    public void setImgid(Integer imgid) {
        this.imgid = imgid;
    }

    public String getEpisodetype() {
        return episodetype;
    }

    public void setEpisodetype(String episodetype) {
        this.episodetype = episodetype;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getEpisodeSeq() {
        return episodeSeq;
    }

    public void setEpisodeSeq(Integer episodeSeq) {
        this.episodeSeq = episodeSeq;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(Integer playbackCount) {
        this.playbackCount = playbackCount;
    }

    public String getIsexplicit() {
        return isexplicit;
    }

    public void setIsexplicit(String isexplicit) {
        this.isexplicit = isexplicit;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
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

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean isService_downloaded() {
        return service_downloaded;
    }

    public void setService_downloaded(boolean service_downloaded) {
        this.service_downloaded = service_downloaded;
    }

    public PodcastPOJO getPodcastPOJO() {
        return podcastPOJO;
    }

    public void setPodcastPOJO(PodcastPOJO podcastPOJO) {
        this.podcastPOJO = podcastPOJO;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }
}
