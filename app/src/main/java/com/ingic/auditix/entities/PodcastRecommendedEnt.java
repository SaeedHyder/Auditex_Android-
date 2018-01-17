package com.ingic.auditix.entities;

/**
 * Created on 12/26/2017.
 */

public class PodcastRecommendedEnt {
    private int albumRes;
    private String albumTitle;

    public PodcastRecommendedEnt(int albumRes, String albumTitle) {
        this.albumRes = albumRes;
        this.albumTitle = albumTitle;
    }

    public int getAlbumRes() {
        return albumRes;
    }

    public void setAlbumRes(int albumRes) {
        this.albumRes = albumRes;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }
}
