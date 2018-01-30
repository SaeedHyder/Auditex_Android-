package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 1/12/2018.
 */

public class SubscribePodcastEnt   {

    @SerializedName("PodcastSubscriptionId")
    private int PodcastSubscriptionId;
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
    @SerializedName("Rating")
    private int Rating;
    @SerializedName("Liked")
    private boolean Liked;
    @SerializedName("PublishDate")
    private String PublishDate;
    @SerializedName("ArtistName")
    @Expose
    private String artistName;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getPodcastSubscriptionId() {
        return PodcastSubscriptionId;
    }

    public void setPodcastSubscriptionId(int PodcastSubscriptionId) {
        this.PodcastSubscriptionId = PodcastSubscriptionId;
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
