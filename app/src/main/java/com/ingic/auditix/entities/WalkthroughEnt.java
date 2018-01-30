package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 1/18/2018.
 */

public class WalkthroughEnt {

    @SerializedName("WalkThroughId")
    private int WalkThroughId;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Description")
    private String Description;
    @SerializedName("ImageUrl")
    private String ImageUrl;
    @SerializedName("SequenceNo")
    private int SequenceNo;
    @SerializedName("BaseUrl")
    private String BaseUrl;

    public int getWalkThroughId() {
        return WalkThroughId;
    }

    public void setWalkThroughId(int WalkThroughId) {
        this.WalkThroughId = WalkThroughId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public int getSequenceNo() {
        return SequenceNo;
    }

    public void setSequenceNo(int SequenceNo) {
        this.SequenceNo = SequenceNo;
    }

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String BaseUrl) {
        this.BaseUrl = BaseUrl;
    }
}
