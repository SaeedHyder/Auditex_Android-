package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/13/2018.
 */

public class SearchEnt {

    @SerializedName("ID")
    private int ID;
    @SerializedName("Name")
    private String Name;
    @SerializedName("NarratedBy")
    private String NarratedBy;
    @SerializedName("Genre")
    private String Genre;
    @SerializedName("Description")
    private String Description;
    @SerializedName("ImageUrl")
    private String ImageUrl;
    @SerializedName("Type")
    private String Type;
    @SerializedName("BaseUrl")
    private String BaseUrl;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNarratedBy() {
        return NarratedBy;
    }

    public void setNarratedBy(String NarratedBy) {
        this.NarratedBy = NarratedBy;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String Genre) {
        this.Genre = Genre;
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

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String BaseUrl) {
        this.BaseUrl = BaseUrl;
    }
}
