package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created on 1/30/2018.
 */

public class BookChaptersEnt extends RealmObject {

    @SerializedName("Chapter")
    @Expose
    private RealmList<BooksChapterItemEnt> chapter = null;
    @SerializedName("WowzaURL")
    @Expose
    private String wowzaURL;
    @SerializedName("WowzaPort")
    @Expose
    private String wowzaPort;
    @SerializedName("WowzaAppName")
    @Expose
    private String wowzaAppName;
    @SerializedName("AudioUrl")
    @Expose
    private String AudioUrl;

    public String getAudioUrl() {
        return AudioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        AudioUrl = audioUrl;
    }

    public RealmList<BooksChapterItemEnt> getChapter() {
        //return new ArrayList<>(chapter.subList(0,chapter.size()));
        return chapter;
    }

    public void setChapter(RealmList<BooksChapterItemEnt> chapter) {
        this.chapter = chapter;
    }

    public String getWowzaURL() {
        return wowzaURL;
    }

    public void setWowzaURL(String wowzaURL) {
        this.wowzaURL = wowzaURL;
    }

    public String getWowzaPort() {
        return wowzaPort;
    }

    public void setWowzaPort(String wowzaPort) {
        this.wowzaPort = wowzaPort;
    }

    public String getWowzaAppName() {
        return wowzaAppName;
    }

    public void setWowzaAppName(String wowzaAppName) {
        this.wowzaAppName = wowzaAppName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(chapter).append(wowzaPort).append(wowzaURL).append(wowzaAppName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BookChaptersEnt)) {
            return false;
        }
        BookChaptersEnt rhs = ((BookChaptersEnt) other);
        return new EqualsBuilder().append(chapter, rhs.chapter).append(wowzaPort, rhs.wowzaPort).append(wowzaURL, rhs.wowzaURL).append(wowzaAppName, rhs.wowzaAppName).isEquals();
    }

}
