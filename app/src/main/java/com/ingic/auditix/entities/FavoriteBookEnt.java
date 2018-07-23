package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 7/23/18.
 */
public class FavoriteBookEnt {
    @SerializedName("Books")
    @Expose
    private ArrayList<BookFavoriteEnt> books = null;
    @SerializedName("BooksCount")
    @Expose
    private Integer booksCount;

    public ArrayList<BookFavoriteEnt> getBooks() {
        return books;
    }

    public Integer getBooksCount() {
        return booksCount;
    }
}
