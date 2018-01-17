package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 1/12/2018.
 */

public class PodcastHomeEnt {
    @SerializedName("FeaturedCategories")
    @Expose
    private ArrayList<PodcastCategoryHomeEnt> featuredCategories = null;
    @SerializedName("DefaultCategories")
    @Expose
    private ArrayList<PodcastCategoryHomeEnt> defaultCategories = null;

    public ArrayList<PodcastCategoryHomeEnt> getFeaturedCategories() {
        return featuredCategories;
    }

    public void setFeaturedCategories(ArrayList<PodcastCategoryHomeEnt> featuredCategories) {
        this.featuredCategories = featuredCategories;
    }

    public ArrayList<PodcastCategoryHomeEnt> getDefaultCategories() {
        return defaultCategories;
    }

    public void setDefaultCategories(ArrayList<PodcastCategoryHomeEnt> defaultCategories) {
        this.defaultCategories = defaultCategories;
    }
}
