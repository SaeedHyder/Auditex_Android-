package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 2/2/2018.
 */

public class BookGenreEnt {
    @SerializedName("GenreID")
    @Expose
    private Integer GenreID;
    @SerializedName("GenreName")
    @Expose
    private String GenreName;

    public Integer getGenreID() {
        return GenreID;
    }

    public void setGenreID(Integer genreID) {
        GenreID = genreID;
    }

    public String getGenreName() {
        return GenreName;
    }

    public void setGenreName(String genreName) {
        GenreName = genreName;
    }
}
