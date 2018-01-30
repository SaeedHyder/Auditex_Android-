package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created on 1/30/2018.
 */

public class BookFavoriteEnt {
    @SerializedName("BookID")
    @Expose
    private Integer bookID;
    @SerializedName("BookName")
    @Expose
    private String bookName;
    @SerializedName("AuthorName")
    @Expose
    private String authorName;
    @SerializedName("Rating")
    @Expose
    private Integer rating;
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("TotalsChapters")
    @Expose
    private Integer totalsChapters;
    @SerializedName("Duration")
    @Expose
    private Integer duration;
    @SerializedName("Size")
    @Expose
    private Integer size;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("IsPaid")
    @Expose
    private Boolean isPaid;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("IsLiked")
    @Expose
    private Boolean isLiked;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getTotalsChapters() {
        return totalsChapters;
    }

    public void setTotalsChapters(Integer totalsChapters) {
        this.totalsChapters = totalsChapters;
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

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
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
        return new HashCodeBuilder().append(genre).append(chapters).append(imageUrl).append(bookID).append(isPaid).append(totalsChapters).append(size).append(duration).append(price).append(isPurchased).append(isFavorite).append(authorName).append(isLiked).append(rating).append(bookName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BookFavoriteEnt)) {
            return false;
        }
        BookFavoriteEnt rhs = ((BookFavoriteEnt) other);
        return new EqualsBuilder().append(genre, rhs.genre).append(chapters, rhs.chapters).append(imageUrl, rhs.imageUrl).append(bookID, rhs.bookID).append(isPaid, rhs.isPaid).append(totalsChapters, rhs.totalsChapters).append(size, rhs.size).append(duration, rhs.duration).append(price, rhs.price).append(isPurchased, rhs.isPurchased).append(isFavorite, rhs.isFavorite).append(authorName, rhs.authorName).append(isLiked, rhs.isLiked).append(rating, rhs.rating).append(bookName, rhs.bookName).isEquals();
    }
}
