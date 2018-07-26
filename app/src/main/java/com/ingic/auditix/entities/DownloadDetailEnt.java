package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 7/26/18.
 */
public class DownloadDetailEnt {

    @SerializedName("BookChapter")
    @Expose
    private ArrayList bookChapter = null;
    @SerializedName("NewsEpisode")
    @Expose
    private ArrayList<NewsEpisodeEnt> newsEpisode = null;
    @SerializedName("PodcastEpisode")
    @Expose
    private ArrayList<PodcastEpisodeEnt> podcastEpisode = null;

    public ArrayList<Object> getBookChapter() {
        return bookChapter;
    }

    public void setBookChapter(ArrayList<Object> bookChapter) {
        this.bookChapter = bookChapter;
    }

    public ArrayList<NewsEpisodeEnt> getNewsEpisode() {
        return newsEpisode;
    }

    public void setNewsEpisode(ArrayList<NewsEpisodeEnt> newsEpisode) {
        this.newsEpisode = newsEpisode;
    }

    public ArrayList<PodcastEpisodeEnt> getPodcastEpisode() {
        return podcastEpisode;
    }

    public void setPodcastEpisode(ArrayList<PodcastEpisodeEnt> podcastEpisode) {
        this.podcastEpisode = podcastEpisode;
    }
}
