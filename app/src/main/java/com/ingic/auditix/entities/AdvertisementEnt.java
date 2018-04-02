package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/22/2018.
 */

public class AdvertisementEnt {

    @SerializedName("AdvertisementId")
    private int AdvertisementId;
    @SerializedName("AdvertisementType")
    private int AdvertisementType;
    @SerializedName("Name")
    private String Name;
    @SerializedName("ImagePath")
    private String ImagePath;
    @SerializedName("Priority")
    private int Priority;
    @SerializedName("SequenceNo")
    private int SequenceNo;
    @SerializedName("AudioPath")
    private String AudioPath;
    @SerializedName("Duration")
    private int Duration;
    @SerializedName("FileSize")
    private int FileSize;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("Deleted")
    private boolean Deleted;
    @SerializedName("InActive")
    private boolean InActive;
    @SerializedName("CreatedBy")
    private int CreatedBy;
    @SerializedName("UpdateBy")
    private int UpdateBy;
    @SerializedName("WowzaURL")
    private String WowzaURL;
    @SerializedName("WowzaPort")
    private String WowzaPort;
    @SerializedName("WowzaAppName")
    private String WowzaAppName;
    @SerializedName("AudioUrl")
    private String AudioUrl;

    public int getAdvertisementId() {
        return AdvertisementId;
    }

    public void setAdvertisementId(int AdvertisementId) {
        this.AdvertisementId = AdvertisementId;
    }

    public int getAdvertisementType() {
        return AdvertisementType;
    }

    public void setAdvertisementType(int AdvertisementType) {
        this.AdvertisementType = AdvertisementType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String ImagePath) {
        this.ImagePath = ImagePath;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int Priority) {
        this.Priority = Priority;
    }

    public int getSequenceNo() {
        return SequenceNo;
    }

    public void setSequenceNo(int SequenceNo) {
        this.SequenceNo = SequenceNo;
    }

    public String getAudioPath() {
        return AudioPath;
    }

    public void setAudioPath(String AudioPath) {
        this.AudioPath = AudioPath;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }

    public int getFileSize() {
        return FileSize;
    }

    public void setFileSize(int FileSize) {
        this.FileSize = FileSize;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public boolean getDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean Deleted) {
        this.Deleted = Deleted;
    }

    public boolean getInActive() {
        return InActive;
    }

    public void setInActive(boolean InActive) {
        this.InActive = InActive;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public int getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(int UpdateBy) {
        this.UpdateBy = UpdateBy;
    }

    public String getWowzaURL() {
        return WowzaURL;
    }

    public void setWowzaURL(String WowzaURL) {
        this.WowzaURL = WowzaURL;
    }

    public String getWowzaPort() {
        return WowzaPort;
    }

    public void setWowzaPort(String WowzaPort) {
        this.WowzaPort = WowzaPort;
    }

    public String getWowzaAppName() {
        return WowzaAppName;
    }

    public void setWowzaAppName(String WowzaAppName) {
        this.WowzaAppName = WowzaAppName;
    }

    public String getAudioUrl() {
        return AudioUrl;
    }

    public void setAudioUrl(String AudioUrl) {
        this.AudioUrl = AudioUrl;
    }
}
