package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 3/13/2018.
 */

public class SearchEnt {

    @SerializedName("Podcasts")
    private ArrayList<PodcastDetailHomeEnt> Podcasts;
    @SerializedName("Books")
    private ArrayList<BookDetailEnt> Books;
    @SerializedName("News")
    private ArrayList<NewsCategoryEnt> News;

    public ArrayList<PodcastDetailHomeEnt> getPodcasts() {
        return Podcasts;
    }

    public void setPodcasts(ArrayList<PodcastDetailHomeEnt> podcasts) {
        Podcasts = podcasts;
    }

    public ArrayList<BookDetailEnt> getBooks() {
        return Books;
    }

    public void setBooks(ArrayList<BookDetailEnt> books) {
        Books = books;
    }

    public ArrayList<NewsCategoryEnt> getNews() {
        return News;
    }

    public void setNews(ArrayList<NewsCategoryEnt> news) {
        News = news;
    }
}
