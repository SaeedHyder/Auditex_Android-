package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/18/18.
 */
public class BookCategoriesEnt {


    @Expose
    @SerializedName("InActive")
    private boolean inactive;
    @Expose
    @SerializedName("Deleted")
    private boolean deleted;
    @Expose
    @SerializedName("UpdatedDate")
    private String updateddate;
    @Expose
    @SerializedName("CreatedDate")
    private String createddate;
    @Expose
    @SerializedName("UpdateBy")
    private int updateby;
    @Expose
    @SerializedName("CreatedBy")
    private int createdby;
    @Expose
    @SerializedName("ImageUrl")
    private String imageurl;
    @Expose
    @SerializedName("IsFeatured")
    private boolean isfeatured;
    @Expose
    @SerializedName("ArabicName")
    private String arabicname;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("CategoryType")
    private int categorytype;
    @Expose
    @SerializedName("SequenceNo")
    private int sequenceno;
    @Expose
    @SerializedName("BookCategoryID")
    private int bookcategoryid;

    public boolean getInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(String updateddate) {
        this.updateddate = updateddate;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public int getUpdateby() {
        return updateby;
    }

    public void setUpdateby(int updateby) {
        this.updateby = updateby;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean getIsfeatured() {
        return isfeatured;
    }

    public void setIsfeatured(boolean isfeatured) {
        this.isfeatured = isfeatured;
    }

    public String getArabicname() {
        return arabicname;
    }

    public void setArabicname(String arabicname) {
        this.arabicname = arabicname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(int categorytype) {
        this.categorytype = categorytype;
    }

    public int getSequenceno() {
        return sequenceno;
    }

    public void setSequenceno(int sequenceno) {
        this.sequenceno = sequenceno;
    }

    public int getBookcategoryid() {
        return bookcategoryid;
    }

    public void setBookcategoryid(int bookcategoryid) {
        this.bookcategoryid = bookcategoryid;
    }
}
