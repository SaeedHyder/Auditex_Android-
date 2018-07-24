package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 7/24/18.
 */
public class PodcastAutoDownloadEnt {
    @SerializedName("TrackId")
    @Expose
    private Integer trackId;
    @SerializedName("PodcastEpisodes")
    @Expose
    private ArrayList<PodcastEpisodeEnt> podcastEpisodes = null;

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public ArrayList<PodcastEpisodeEnt> getPodcastEpisodes() {
        return podcastEpisodes;
    }

    public void setPodcastEpisodes(ArrayList<PodcastEpisodeEnt> podcastEpisodes) {
        this.podcastEpisodes = podcastEpisodes;
    }
}
