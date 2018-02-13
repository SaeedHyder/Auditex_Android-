package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created on 1/10/2018.
 */

public class PodcastTrackEnt {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DateAdded")
    @Expose
    private String dateAdded;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("FileUrl")
    @Expose
    private String fileUrl;
    private boolean isSelected;
    private int statusState;
    private int downloadProgress = 0;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(duration).append(imageUrl).append(description).append(name).append(dateAdded).append(fileUrl).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PodcastTrackEnt)) {
            return false;
        }
        PodcastTrackEnt rhs = ((PodcastTrackEnt) other);
        return new EqualsBuilder().append(Id, rhs.Id).isEquals();
    }
}
