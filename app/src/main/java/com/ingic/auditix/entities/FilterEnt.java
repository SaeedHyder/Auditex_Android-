package com.ingic.auditix.entities;

/**
 * Created on 2/2/2018.
 */

public class FilterEnt {
    private String GenreID;
    private String GenreName;

    public FilterEnt(String genreID, String genreName) {
        GenreID = genreID;
        GenreName = genreName;
    }

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
