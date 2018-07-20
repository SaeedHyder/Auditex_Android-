package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/20/18.
 */
public class DurationEnt {
    @SerializedName("MinDuration")
    @Expose
    private Integer minDuration;
    @SerializedName("MaxDuration")
    @Expose
    private Integer maxDuration;
    @SerializedName("MinSubscriber")
    @Expose
    private Integer minSubscriber;
    @SerializedName("MaxSubscriber")
    @Expose
    private Integer maxSubscriber;

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMinSubscriber() {
        return minSubscriber;
    }

    public void setMinSubscriber(Integer minSubscriber) {
        this.minSubscriber = minSubscriber;
    }

    public Integer getMaxSubscriber() {
        return maxSubscriber;
    }

    public void setMaxSubscriber(Integer maxSubscriber) {
        this.maxSubscriber = maxSubscriber;
    }
}
