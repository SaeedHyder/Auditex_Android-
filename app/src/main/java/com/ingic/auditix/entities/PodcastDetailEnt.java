package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created on 1/10/2018.
 */

public class PodcastDetailEnt {
    @SerializedName("WowzaURL")
    @Expose

    public String WowzaURL;
    @SerializedName("WowzaPort")
    @Expose
    public String WowzaPort;
    @SerializedName("WowzaAppName")
    @Expose
    public String WowzaAppName;
    @SerializedName("IsEpisodeAdded")
    @Expose
    public boolean IsEpisodeAdded;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("PublishDate")
    @Expose
    private String publishDate;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("Author")
    @Expose
    private String author;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Rating")
    @Expose
    private Integer Rating;
    @SerializedName("IsSubscribed")
    @Expose
    private boolean IsSubscribed;
    @SerializedName("IsFavorite")
    @Expose
    private boolean IsFavorite;
    @SerializedName("TrackList")
    @Expose
    private ArrayList<PodcastTrackEnt> trackList = null;

    public String getWowzaURL() {
        return WowzaURL;
    }

    public void setWowzaURL(String wowzaURL) {
        WowzaURL = wowzaURL;
    }

    public String getWowzaPort() {
        return WowzaPort;
    }

    public void setWowzaPort(String wowzaPort) {
        WowzaPort = wowzaPort;
    }

    public String getWowzaAppName() {
        return WowzaAppName;
    }

    public void setWowzaAppName(String wowzaAppName) {
        WowzaAppName = wowzaAppName;
    }

    public boolean isEpisodeAdded() {
        return IsEpisodeAdded;
    }

    public void setEpisodeAdded(boolean episodeAdded) {
        IsEpisodeAdded = episodeAdded;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer rating) {
        Rating = rating;
    }

    public boolean isSubscribed() {
        return IsSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        IsSubscribed = subscribed;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<PodcastTrackEnt> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<PodcastTrackEnt> trackList) {
        this.trackList = trackList;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(author).append(title).append(description).append(image).append(language).append(trackList).append(publishDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PodcastDetailEnt)) {
            return false;
        }
        PodcastDetailEnt rhs = ((PodcastDetailEnt) other);
        return new EqualsBuilder().append(author, rhs.author).append(title, rhs.title).append(description, rhs.description).append(image, rhs.image).append(language, rhs.language).append(trackList, rhs.trackList).append(publishDate, rhs.publishDate).isEquals();
    }

}
