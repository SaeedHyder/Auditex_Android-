package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 3/15/2018.
 */

public class NewsEpisodeEnt extends RealmObject {

    @Expose
    @SerializedName("CreatedDate")
    private String createddate;
    @Expose
    @SerializedName("EpisodeTitle")
    private String episodetitle;
    @Expose
    @SerializedName("EpisodeWebUrl")
    private String episodeweburl;
    @Expose
    @SerializedName("FileDuration")
    private int fileduration;
    @Expose
    @SerializedName("FilePath")
    private String filepath;
    @Expose
    @SerializedName("FileSize")
    private int filesize;
    @Expose
    @SerializedName("EpisodeNo")
    private int episodeno;
    @Expose
    @SerializedName("NewsID")
    private int newsid;
    @PrimaryKey
    @Expose
    @SerializedName("NewsEpisodeID")
    private String newsepisodeid;
    private int statusState;
    private int downloadProgress = 0;

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

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getEpisodetitle() {
        return episodetitle;
    }

    public void setEpisodetitle(String episodetitle) {
        this.episodetitle = episodetitle;
    }

    public String getEpisodeweburl() {
        return episodeweburl;
    }

    public void setEpisodeweburl(String episodeweburl) {
        this.episodeweburl = episodeweburl;
    }

    public int getFileduration() {
        return fileduration;
    }

    public void setFileduration(int fileduration) {
        this.fileduration = fileduration;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public int getEpisodeno() {
        return episodeno;
    }

    public void setEpisodeno(int episodeno) {
        this.episodeno = episodeno;
    }

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public String getNewsepisodeid() {
        return newsepisodeid;
    }

    public void setNewsepisodeid(String newsepisodeid) {
        this.newsepisodeid = newsepisodeid;
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NewsEpisodeEnt)) {
            return false;
        }
        NewsEpisodeEnt rhs = ((NewsEpisodeEnt) other);
        return new EqualsBuilder().append(newsepisodeid, rhs.newsepisodeid).isEquals();
    }
}