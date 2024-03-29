package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 2/2/2018.
 */

public class BookGenreEnt {
    @SerializedName("GenreID")
    @Expose
    private String GenreID;
    @SerializedName("GenreName")
    @Expose
    private String GenreName;

    public String getGenreID() {
        return GenreID;
    }

    public void setGenreID(String genreID) {
        GenreID = genreID;
    }

    public String getGenreName() {
        return GenreName;
    }

    public void setGenreName(String genreName) {
        GenreName = genreName;
    }
}
