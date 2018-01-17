package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created on 1/10/2018.
 */

public class PodcastCategoryDetailEnt {
    @SerializedName("CollectionId")
    @Expose
    private Integer collectionId;
    @SerializedName("TrackId")
    @Expose
    private Integer trackId;
    @SerializedName("ArtistName")
    @Expose
    private String artistName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("ReleaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("EpisodeCount")
    @Expose
    private Integer episodeCount;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("Rating")
    @Expose
    private Integer rating;
    @SerializedName("FeedUrl")
    @Expose
    private String feedUrl;

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(genre).append(trackId).append(feedUrl).append(releaseDate).append(imageUrl).append(collectionId).append(episodeCount).append(name).append(rating).append(artistName).append(country).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PodcastCategoryDetailEnt) == false) {
            return false;
        }
        PodcastCategoryDetailEnt rhs = ((PodcastCategoryDetailEnt) other);
        return new EqualsBuilder().append(genre, rhs.genre).append(trackId, rhs.trackId).append(feedUrl, rhs.feedUrl).append(releaseDate, rhs.releaseDate).append(imageUrl, rhs.imageUrl).append(collectionId, rhs.collectionId).append(episodeCount, rhs.episodeCount).append(name, rhs.name).append(rating, rhs.rating).append(artistName, rhs.artistName).append(country, rhs.country).isEquals();
    }
}
