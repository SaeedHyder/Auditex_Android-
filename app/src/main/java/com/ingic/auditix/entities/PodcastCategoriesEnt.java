package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created on 1/16/2018.
 */

public class PodcastCategoriesEnt {

    @SerializedName("PodcastCategoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("Title")
    @Expose
    private String title;

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    @SerializedName("ImagePath")
    @Expose
    private String ImagePath;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(categoryId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PodcastCategoriesEnt) == false) {
            return false;
        }
        PodcastCategoriesEnt rhs = ((PodcastCategoriesEnt) other);
        return new EqualsBuilder().append(title, rhs.title).append(categoryId, rhs.categoryId).isEquals();
    }

}
