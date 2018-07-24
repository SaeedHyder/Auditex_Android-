package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 7/24/18.
 */
public class AutoDownloadEnt {

    @SerializedName("Podcast")
    @Expose
    private ArrayList<PodcastAutoDownloadEnt> podcast = null;
    @SerializedName("News")
    @Expose
    private ArrayList<NewsEpisodeEnt> news = null;

    public ArrayList<PodcastAutoDownloadEnt> getPodcast() {
        return podcast;
    }

    public void setPodcast(ArrayList<PodcastAutoDownloadEnt> podcast) {
        this.podcast = podcast;
    }

    public ArrayList<NewsEpisodeEnt> getNews() {
        return news;
    }

    public void setNews(ArrayList<NewsEpisodeEnt> news) {
        this.news = news;
    }
}
