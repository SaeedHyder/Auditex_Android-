package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 1/15/2018.
 */

public class PodcastFavoriteEnt {

    @SerializedName("FavoritePodcastId")
    private int FavoritePodcastId;
    @SerializedName("AccountId")
    private int AccountId;
    @SerializedName("CollectionId")
    private int CollectionId;
    @SerializedName("TrackId")
    private int TrackId;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Description")
    private String Description;
    @SerializedName("ImageUrl")
    private String ImageUrl;
    @SerializedName("FeedUrl")
    private String FeedUrl;
    @SerializedName("ArtistName")
    private String ArtistName;
    @SerializedName("Rating")
    private int Rating;
    @SerializedName("Liked")
    private boolean Liked;
    @SerializedName("PublishDate")
    private String PublishDate;

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public int getFavoritePodcastId() {
        return FavoritePodcastId;
    }

    public void setFavoritePodcastId(int FavoritePodcastId) {
        this.FavoritePodcastId = FavoritePodcastId;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int AccountId) {
        this.AccountId = AccountId;
    }

    public int getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(int CollectionId) {
        this.CollectionId = CollectionId;
    }

    public int getTrackId() {
        return TrackId;
    }

    public void setTrackId(int TrackId) {
        this.TrackId = TrackId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getFeedUrl() {
        return FeedUrl;
    }

    public void setFeedUrl(String FeedUrl) {
        this.FeedUrl = FeedUrl;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int Rating) {
        this.Rating = Rating;
    }

    public boolean getLiked() {
        return Liked;
    }

    public void setLiked(boolean Liked) {
        this.Liked = Liked;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String PublishDate) {
        this.PublishDate = PublishDate;
    }
}
