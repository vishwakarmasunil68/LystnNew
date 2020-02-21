package com.app.lystn.pojo;

import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;

import java.io.Serializable;
import java.util.List;

public class DownloadPOJO implements Serializable {
    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS;

    public List<PodcastEpisodeDetailsPOJO> getPodcastEpisodeDetailsPOJOS() {
        return podcastEpisodeDetailsPOJOS;
    }

    public void setPodcastEpisodeDetailsPOJOS(List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS) {
        this.podcastEpisodeDetailsPOJOS = podcastEpisodeDetailsPOJOS;
    }
}
