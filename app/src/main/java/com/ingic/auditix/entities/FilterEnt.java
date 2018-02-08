package com.ingic.auditix.entities;

/**
 * Created on 2/2/2018.
 */

public class FilterEnt {
    private Integer GenreID;
    private String GenreName;

    public FilterEnt(Integer genreID, String genreName) {
        GenreID = genreID;
        GenreName = genreName;
    }

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
