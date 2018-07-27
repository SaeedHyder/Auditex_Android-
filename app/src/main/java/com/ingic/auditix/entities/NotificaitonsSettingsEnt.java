package com.ingic.auditix.entities;

import java.util.ArrayList;

public class NotificaitonsSettingsEnt {

    ArrayList<News> News=new ArrayList<>();
    ArrayList<Podcast> Podcast=new ArrayList<>();

    public ArrayList<com.ingic.auditix.entities.News> getNews() {
        return News;
    }

    public void setNews(ArrayList<com.ingic.auditix.entities.News> news) {
        News = news;
    }

    public ArrayList<com.ingic.auditix.entities.Podcast> getPodcast() {
        return Podcast;
    }

    public void setPodcast(ArrayList<com.ingic.auditix.entities.Podcast> podcast) {
        Podcast = podcast;
    }
}
