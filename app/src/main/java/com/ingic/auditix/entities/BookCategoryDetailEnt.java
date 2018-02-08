package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created on 2/1/2018.
 */

public class BookCategoryDetailEnt {
    @SerializedName("Books")
    @Expose
    private ArrayList<BookDetailEnt> books = null;
    @SerializedName("BooksCount")
    @Expose
    private Integer booksCount;

    public ArrayList<BookDetailEnt> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BookDetailEnt> books) {
        this.books = books;
    }

    public Integer getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(Integer booksCount) {
        this.booksCount = booksCount;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(books).append(booksCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BookCategoryDetailEnt) == false) {
            return false;
        }
        BookCategoryDetailEnt rhs = ((BookCategoryDetailEnt) other);
        return new EqualsBuilder().append(books, rhs.books).append(booksCount, rhs.booksCount).isEquals();
    }

}
