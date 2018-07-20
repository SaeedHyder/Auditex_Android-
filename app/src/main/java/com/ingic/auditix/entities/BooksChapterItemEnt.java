package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.realm.RealmObject;

/**
 * Created on 1/30/2018.
 */

public class BooksChapterItemEnt extends RealmObject {

    @SerializedName("ChapterID")
    @Expose
    private String chapterID;
    @SerializedName("ChapterNumber")
    @Expose
    private Integer chapterNumber;
    @SerializedName("AudioUrl")
    @Expose
    private String audioUrl;
    @SerializedName("FileDuration")
    @Expose
    private Integer fileDuration;
    @SerializedName("EpisodeWebUrl")
    @Expose
    private String episodeWebUrl;
    @SerializedName("FilePath")
    @Expose
    private String filePath;
    private boolean isSelected;
    private int statusState;
    private int downloadProgress = 0;

    public String getEpisodeWebUrl() {
        return episodeWebUrl;
    }

    public void setEpisodeWebUrl(String episodeWebUrl) {
        this.episodeWebUrl = episodeWebUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStatusState() {
        return statusState;
    }

    public void setStatusState(int statusState) {
        this.statusState = statusState;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getChapterID() {
        return chapterID;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public Integer getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(Integer chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Integer getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(Integer fileDuration) {
        this.fileDuration = fileDuration;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(audioUrl).append(chapterNumber).append(chapterID).append(fileDuration).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BooksChapterItemEnt)) {
            return false;
        }
        BooksChapterItemEnt rhs = ((BooksChapterItemEnt) other);
        return new EqualsBuilder().append(chapterID, rhs.chapterID).isEquals();
    }
}
