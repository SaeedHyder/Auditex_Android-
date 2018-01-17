package com.ingic.auditix.entities;

/**
 * Created on 12/15/2017.
 */

public class NavigationEnt {
    private int ResId;
    private String Title;
    private int count;

    public NavigationEnt(int resId, String title, int count) {
        ResId = resId;
        Title = title;
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getResId() {

        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
