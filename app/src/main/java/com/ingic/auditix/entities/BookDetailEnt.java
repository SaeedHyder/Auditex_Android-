package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 2/1/2018.
 */

public class BookDetailEnt extends RealmObject{
    @PrimaryKey
    @SerializedName("BookID")
    @Expose
    private Integer bookID;
    @SerializedName("BookName")
    @Expose
    private String bookName;
    @SerializedName("AuthorName")
    @Expose
    private String authorName;
    @SerializedName("NarratorName")
    @Expose
    private String narratorName;
    @SerializedName("Rating")
    @Expose
    private Float rating;
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("TotalsChapters")
    @Expose
    private int totalChapters;
    @SerializedName("Duration")
    @Expose
    private Integer duration;
    @SerializedName("Size")
    @Expose
    private Integer size;
    @SerializedName("AboutTheBook")
    @Expose
    private String aboutTheBook;
    @SerializedName("AboutTheNarrator")
    @Expose
    private String aboutTheNarrator;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("IsPaid")
    @Expose
    private Boolean isPaid;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("IsPurchased")
    @Expose
    private Boolean isPurchased;
    @SerializedName("IsFavorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("Chapters")
    @Expose
    private BookChaptersEnt chapters;

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getNarratorName() {
        return narratorName;
    }

    public void setNarratorName(String narratorName) {
        this.narratorName = narratorName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAboutTheBook() {
        return aboutTheBook;
    }

    public void setAboutTheBook(String aboutTheBook) {
        this.aboutTheBook = aboutTheBook;
    }

    public String getAboutTheNarrator() {
        return aboutTheNarrator;
    }

    public void setAboutTheNarrator(String aboutTheNarrator) {
        this.aboutTheNarrator = aboutTheNarrator;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public BookChaptersEnt getChapters() {
        return chapters;
    }

    public void setChapters(BookChaptersEnt chapters) {
        this.chapters = chapters;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(genre).append(chapters).append(imageUrl).append(bookID).append(isPaid).append(aboutTheNarrator).append(size).append(narratorName).append(duration).append(price).append(isPurchased).append(isFavorite).append(authorName).append(aboutTheBook).append(rating).append(totalChapters).append(bookName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BookDetailEnt) == false) {
            return false;
        }
        BookDetailEnt rhs = ((BookDetailEnt) other);
        return new EqualsBuilder().append(genre, rhs.genre).append(chapters, rhs.chapters).append(imageUrl, rhs.imageUrl).append(bookID, rhs.bookID).append(isPaid, rhs.isPaid).append(aboutTheNarrator, rhs.aboutTheNarrator).append(size, rhs.size).append(narratorName, rhs.narratorName).append(duration, rhs.duration).append(price, rhs.price).append(isPurchased, rhs.isPurchased).append(isFavorite, rhs.isFavorite).append(authorName, rhs.authorName).append(aboutTheBook, rhs.aboutTheBook).append(rating, rhs.rating).append(totalChapters, rhs.totalChapters).append(bookName, rhs.bookName).isEquals();
    }

}
