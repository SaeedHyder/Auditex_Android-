package com.ingic.auditix.entities;

/**
 * Created on 12/28/2017.
 */

public class FilterChildEnt {
    private String rangeMinValue;
    private String rangeMaxValue;
    private String genreTitle;

    public FilterChildEnt(String rangeMinValue, String rangeMaxValue, String genreTitle) {
        this.rangeMinValue = rangeMinValue;
        this.rangeMaxValue = rangeMaxValue;
        this.genreTitle = genreTitle;
    }

    public String getRangeMinValue() {
        return rangeMinValue;
    }

    public void setRangeMinValue(String rangeMinValue) {
        this.rangeMinValue = rangeMinValue;
    }

    public String getRangeMaxValue() {
        return rangeMaxValue;
    }

    public void setRangeMaxValue(String rangeMaxValue) {
        this.rangeMaxValue = rangeMaxValue;
    }

    public String getGenreTitle() {
        return genreTitle;
    }

    public void setGenreTitle(String genreTitle) {
        this.genreTitle = genreTitle;
    }
}
