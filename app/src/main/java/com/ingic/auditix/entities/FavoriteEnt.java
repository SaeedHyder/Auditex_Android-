package com.ingic.auditix.entities;

/**
 * Created on 1/5/2018.
 */

public class FavoriteEnt {
    private String albumImage;
    private String albumTitle;
    private String albumNarrator;

    public FavoriteEnt(String albumImage, String albumTitle, String albumNarrator) {
        this.albumImage = albumImage;
        this.albumTitle = albumTitle;
        this.albumNarrator = albumNarrator;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumNarrator() {
        return albumNarrator;
    }

    public void setAlbumNarrator(String albumNarrator) {
        this.albumNarrator = albumNarrator;
    }
}
