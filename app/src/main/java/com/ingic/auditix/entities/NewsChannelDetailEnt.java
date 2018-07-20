package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 7/16/18.
 */
public class NewsChannelDetailEnt {

    @Expose
    @SerializedName("NewsEpisodesLater")
    private List<NewsEpisodeEnt> newsepisodeslater;
    @Expose
    @SerializedName("NewsEpisodesYesterday")
    private List<NewsEpisodeEnt> newsepisodesyesterday;
    @Expose
    @SerializedName("NewsEpisodesToday")
    private List<NewsEpisodeEnt> newsepisodestoday;

    public List<NewsEpisodeEnt> getNewsepisodeslater() {
        return newsepisodeslater;
    }

    public void setNewsepisodeslater(List<NewsEpisodeEnt> newsepisodeslater) {
        this.newsepisodeslater = newsepisodeslater;
    }

    public List<NewsEpisodeEnt> getNewsepisodesyesterday() {
        return newsepisodesyesterday;
    }

    public void setNewsepisodesyesterday(List<NewsEpisodeEnt> newsepisodesyesterday) {
        this.newsepisodesyesterday = newsepisodesyesterday;
    }

    public List<NewsEpisodeEnt> getNewsepisodestoday() {
        return newsepisodestoday;
    }

    public void setNewsepisodestoday(List<NewsEpisodeEnt> newsepisodestoday) {
        this.newsepisodestoday = newsepisodestoday;
    }
}
