package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 4/11/18.
 */
public class PodcastLocationEnt {

    @Expose
    @SerializedName("InActive")
    private boolean inactive;
    @Expose
    @SerializedName("Deleted")
    private boolean deleted;
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
    @SerializedName("CountryName")
    private String countryname;
    @Expose
    @SerializedName("CountryCode")
    private String countrycode;
    @Expose
    @SerializedName("Id")
    private int id;

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

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
