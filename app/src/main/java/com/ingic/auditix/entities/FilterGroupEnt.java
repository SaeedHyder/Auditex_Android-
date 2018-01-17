package com.ingic.auditix.entities;

/**
 * Created on 12/28/2017.
 */

public class FilterGroupEnt {
    private String groupTitle;

    public FilterGroupEnt(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
