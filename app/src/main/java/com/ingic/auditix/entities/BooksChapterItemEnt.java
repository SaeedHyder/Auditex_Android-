package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created on 1/30/2018.
 */

public class BooksChapterItemEnt {

    @SerializedName("ChapterID")
    @Expose
    private Integer chapterID;
    @SerializedName("ChapterNumber")
    @Expose
    private Integer chapterNumber;
    @SerializedName("AudioUrl")
    @Expose
    private String audioUrl;
    @SerializedName("FileDuration")
    @Expose
    private Integer fileDuration;

    public Integer getChapterID() {
        return chapterID;
    }

    public void setChapterID(Integer chapterID) {
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
        return new EqualsBuilder().append(audioUrl, rhs.audioUrl).append(chapterNumber, rhs.chapterNumber).append(chapterID, rhs.chapterID).append(fileDuration, rhs.fileDuration).isEquals();
    }
}
