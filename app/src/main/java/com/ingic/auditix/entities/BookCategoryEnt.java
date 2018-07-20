package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created on 2/1/2018.
 */

public class BookCategoryEnt {
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("BooksCount")
    @Expose
    private Integer BooksCount;
    @SerializedName("Books")
    @Expose
    private ArrayList<BookDetailEnt> categoryBooks = null;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return BooksCount;
    }

    public void setCategoryId(Integer categoryId) {
        this.BooksCount = categoryId;
    }

    public ArrayList<BookDetailEnt> getCategoryBooks() {
        return categoryBooks;
    }

    public void setCategoryBooks(ArrayList<BookDetailEnt> categoryBooks) {
        this.categoryBooks = categoryBooks;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(categoryName).append(categoryBooks).append(BooksCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BookCategoryEnt) == false) {
            return false;
        }
        BookCategoryEnt rhs = ((BookCategoryEnt) other);
        return new EqualsBuilder().append(categoryName, rhs.categoryName).append(categoryBooks, rhs.categoryBooks).append(BooksCount, rhs.BooksCount).isEquals();
    }
}
