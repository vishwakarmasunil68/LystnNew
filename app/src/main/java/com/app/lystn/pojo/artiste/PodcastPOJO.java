package com.app.lystn.pojo.artiste;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PodcastPOJO implements Serializable {
    @SerializedName("podcast_details")
    PodcastDetailPOJO podcastDetailPOJO;
    @SerializedName("podcast_episode_details")
    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS;

    public PodcastDetailPOJO getPodcastDetailPOJO() {
        return podcastDetailPOJO;
    }

    public void setPodcastDetailPOJO(PodcastDetailPOJO podcastDetailPOJO) {
        this.podcastDetailPOJO = podcastDetailPOJO;
    }

    public List<PodcastEpisodeDetailsPOJO> getPodcastEpisodeDetailsPOJOS() {
        return podcastEpisodeDetailsPOJOS;
    }

    public void setPodcastEpisodeDetailsPOJOS(List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS) {
        this.podcastEpisodeDetailsPOJOS = podcastEpisodeDetailsPOJOS;
    }
}
