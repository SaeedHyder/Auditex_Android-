package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {


    @SerializedName("NewsID")
    @Expose
    private Integer newsID;
    @SerializedName("NewsCategoryID")
    @Expose
    private Integer newsCategoryID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("NarratorName")
    @Expose
    private String narratorName;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("IsEpisodesAdded")
    @Expose
    private Boolean isEpisodesAdded;
    @SerializedName("EpisodeCount")
    @Expose
    private Integer episodeCount;
    @SerializedName("IsInternational")
    @Expose
    private Boolean isInternational;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("IsSubscribed")
    @Expose
    private Boolean isSubscribed;
    @SerializedName("IsFavorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("AutoDownload")
    @Expose
    private Boolean autoDownload;
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

    public Integer getNewsID() {
        return newsID;
    }

    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

    public Integer getNewsCategoryID() {
        return newsCategoryID;
    }

    public void setNewsCategoryID(Integer newsCategoryID) {
        this.newsCategoryID = newsCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNarratorName() {
        return narratorName;
    }

    public void setNarratorName(String narratorName) {
        this.narratorName = narratorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getEpisodesAdded() {
        return isEpisodesAdded;
    }

    public void setEpisodesAdded(Boolean episodesAdded) {
        isEpisodesAdded = episodesAdded;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Boolean getInternational() {
        return isInternational;
    }

    public void setInternational(Boolean international) {
        isInternational = international;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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

    public Boolean getAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(Boolean autoDownload) {
        this.autoDownload = autoDownload;
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
