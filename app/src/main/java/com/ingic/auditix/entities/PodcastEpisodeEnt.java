package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/11/18.
 */
public class PodcastEpisodeEnt {
    @SerializedName("PodcastEpisodeID")
    @Expose
    private Integer podcastEpisodeID;
    @SerializedName("PodcastId")
    @Expose
    private Integer podcastId;
    @SerializedName("EpisodeNo")
    @Expose
    private Integer episodeNo;
    @SerializedName("EpisodeTitle")
    @Expose
    private String episodeTitle;
    @SerializedName("FilePath")
    @Expose
    private String filePath;
    @SerializedName("FileSize")
    @Expose
    private Integer fileSize;
    @SerializedName("FileDuration")
    @Expose
    private Integer fileDuration;
    @SerializedName("Podcast")
    @Expose
    private PodcastDetailHomeEnt podcast;
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
    @SerializedName("EpisodeWebUrl")
    @Expose
    private String episodeWebUrl;

    public String getEpisodeWebUrl() {
        return episodeWebUrl;
    }

    public void setEpisodeWebUrl(String episodeWebUrl) {
        this.episodeWebUrl = episodeWebUrl;
    }

    public Integer getPodcastEpisodeID() {
        return podcastEpisodeID;
    }

    public void setPodcastEpisodeID(Integer podcastEpisodeID) {
        this.podcastEpisodeID = podcastEpisodeID;
    }

    public Integer getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Integer podcastId) {
        this.podcastId = podcastId;
    }

    public Integer getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Integer episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(Integer fileDuration) {
        this.fileDuration = fileDuration;
    }

    public PodcastDetailHomeEnt getPodcast() {
        return podcast;
    }

    public void setPodcast(PodcastDetailHomeEnt podcast) {
        this.podcast = podcast;
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
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PodcastEpisodeEnt)) {
            return false;
        }
        PodcastEpisodeEnt other = (PodcastEpisodeEnt) obj;
        return podcastEpisodeID.intValue() == other.podcastEpisodeID.intValue();
    }
}
