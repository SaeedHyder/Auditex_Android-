package com.ingic.auditix.entities;

/**
 * Created on 12/27/2017.
 */

public class MySubscriptionsEnt {
    private int albumRes;
    private String title;
    private String narratorName;

    public MySubscriptionsEnt(int albumRes, String title, String narratorName) {
        this.albumRes = albumRes;
        this.title = title;
        this.narratorName = narratorName;
    }

    public int getAlbumRes() {
        return albumRes;
    }

    public void setAlbumRes(int albumRes) {
        this.albumRes = albumRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNarratorName() {
        return narratorName;
    }

    public void setNarratorName(String narratorName) {
        this.narratorName = narratorName;
    }
}
