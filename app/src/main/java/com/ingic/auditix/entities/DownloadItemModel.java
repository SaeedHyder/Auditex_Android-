package com.ingic.auditix.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 2/13/2018.
 */

public class DownloadItemModel extends RealmObject {
    private Integer downloadID;
    private String downloadTag;
    private Integer downloadProgress;
    private Integer downloadState;
    private Integer downloadedBytes;
    private Integer totalBytes;

    public Integer getDownloadID() {
        return downloadID;
    }

    public void setDownloadID(Integer downloadID) {
        this.downloadID = downloadID;
    }

    public String getDownloadTag() {
        return downloadTag;
    }

    public void setDownloadTag(String downloadTag) {
        this.downloadTag = downloadTag;
    }

    public Integer getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(Integer downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public Integer getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(Integer downloadState) {
        this.downloadState = downloadState;
    }

    public Integer getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(Integer downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public Integer getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(Integer totalBytes) {
        this.totalBytes = totalBytes;
    }
}
