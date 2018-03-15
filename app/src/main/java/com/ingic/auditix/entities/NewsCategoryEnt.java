package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/14/2018.
 */

public class NewsCategoryEnt {

    @SerializedName("Id")
    private int Id;
    @SerializedName("SourceImageUrl")
    private String SourceImageUrl;
    @SerializedName("SourceName")
    private String SourceName;
    @SerializedName("CreatedBy")
    private int CreatedBy;
    @SerializedName("UpdateBy")
    private int UpdateBy;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("Deleted")
    private boolean Deleted;
    @SerializedName("InActive")
    private boolean InActive;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getSourceImageUrl() {
        return SourceImageUrl;
    }

    public void setSourceImageUrl(String SourceImageUrl) {
        this.SourceImageUrl = SourceImageUrl;
    }

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String SourceName) {
        this.SourceName = SourceName;
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
}
