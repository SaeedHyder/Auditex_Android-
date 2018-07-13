package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/10/2018.
 */

public class PodcastCategoryListEnt {
    @SerializedName("TotalCount")
    @Expose
    private Integer resultCount;
    @SerializedName("podcastListResultlist")
    @Expose
    private ArrayList<PodcastDetailHomeEnt> results = null;

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public ArrayList<PodcastDetailHomeEnt> getResults() {
        return results;
    }

    public void setResults(ArrayList<PodcastDetailHomeEnt> results) {
        this.results = results;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).append(resultCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PodcastCategoryListEnt) == false) {
            return false;
        }
        PodcastCategoryListEnt rhs = ((PodcastCategoryListEnt) other);
        return new EqualsBuilder().append(results, rhs.results).append(resultCount, rhs.resultCount).isEquals();
    }

}

