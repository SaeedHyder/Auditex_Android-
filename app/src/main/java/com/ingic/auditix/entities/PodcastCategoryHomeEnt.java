package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 1/12/2018.
 */

public class PodcastCategoryHomeEnt {

    @SerializedName("CategoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Podcastdetails")
    @Expose
    private ArrayList<PodcastDetailHomeEnt> podcastdetails = null;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<PodcastDetailHomeEnt> getPodcastdetails() {
        return podcastdetails;
    }

    public void setPodcastdetails(ArrayList<PodcastDetailHomeEnt> podcastdetails) {
        this.podcastdetails = podcastdetails;
    }
}
