package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 3/13/2018.
 */

public class CMSEnt extends RealmObject {
    @PrimaryKey
    @SerializedName("ID")
    private int ID;
    @SerializedName("Key")
    private int Key;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Value")
    private String Value;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getKey() {
        return Key;
    }

    public void setKey(int Key) {
        this.Key = Key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
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
}
