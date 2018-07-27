package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Podcast {


    @SerializedName("PodcastId")
    @Expose
    private Integer podcastId;
    @SerializedName("CategoryId")
    @Expose
    private Integer categoryId;
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
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("EpisodeCount")
    @Expose
    private Integer episodeCount;
    @SerializedName("ReleaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("FeedUrl")
    @Expose
    private Object feedUrl;
    @SerializedName("IsEpisodesAdded")
    @Expose
    private Boolean isEpisodesAdded;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("Rating")
    @Expose
    private Integer rating;
    @SerializedName("IsSubscribed")
    @Expose
    private Boolean isSubscribed;
    @SerializedName("IsFavorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("PodcastCategory")
    @Expose
    private Object podcastCategory;
    @SerializedName("AutoDownload")
    @Expose
    private Boolean autoDownload;
    @SerializedName("IsRated")
    @Expose
    private Boolean isRated;
    @SerializedName("NotificationEnabled")
    @Expose
    private Boolean notificationEnabled;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("UpdateBy")
    @Expose
    private Integer updateBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("Deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("InActive")
    @Expose
    private Boolean inActive;

    public Integer getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Integer podcastId) {
        this.podcastId = podcastId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Object getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(Object feedUrl) {
        this.feedUrl = feedUrl;
    }

    public Boolean getEpisodesAdded() {
        return isEpisodesAdded;
    }

    public void setEpisodesAdded(Boolean episodesAdded) {
        isEpisodesAdded = episodesAdded;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        isSubscribed = subscribed;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Object getPodcastCategory() {
        return podcastCategory;
    }

    public void setPodcastCategory(Object podcastCategory) {
        this.podcastCategory = podcastCategory;
    }

    public Boolean getAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(Boolean autoDownload) {
        this.autoDownload = autoDownload;
    }

    public Boolean getRated() {
        return isRated;
    }

    public void setRated(Boolean rated) {
        isRated = rated;
    }

    public Boolean getNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(Boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getInActive() {
        return inActive;
    }

    public void setInActive(Boolean inActive) {
        this.inActive = inActive;
    }
}
