package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 7/20/18.
 */
public class PodcastFilterEnt {
    @SerializedName("Locations")
    @Expose
    private List<LocationEnt> locations = null;
    @SerializedName("MinMaxSubscibersAndDuration")
    @Expose
    private DurationEnt minMaxSubscibersAndDuration;
    @SerializedName("EntityModel")
    @Expose
    private List<PodcastFilterItemEnt> entityModel = null;

    public List<LocationEnt> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationEnt> locations) {
        this.locations = locations;
    }

    public DurationEnt getMinMaxSubscibersAndDuration() {
        return minMaxSubscibersAndDuration;
    }

    public void setMinMaxSubscibersAndDuration(DurationEnt minMaxSubscibersAndDuration) {
        this.minMaxSubscibersAndDuration = minMaxSubscibersAndDuration;
    }

    public ArrayList<PodcastFilterItemEnt> getEntityModel() {
        return new ArrayList<>(entityModel);
    }

    public void setEntityModel(List<PodcastFilterItemEnt> entityModel) {
        this.entityModel = entityModel;
    }
}
