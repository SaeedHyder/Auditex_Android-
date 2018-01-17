package com.ingic.auditix.entities;

/**
 * Created on 12/21/2017.
 */

public class CartEnt {
    private int itemImage;
    private String title;
    private String narrator;
    private String author;
    private String genre;
    private String price;

    public CartEnt(int itemImage, String title, String narrator, String author, String genre, String price) {
        this.itemImage = itemImage;
        this.title = title;
        this.narrator = narrator;
        this.author = author;
        this.genre = genre;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getNarrator() {
        return narrator;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPrice() {
        return price;
    }

    public int getItemImage() {
        return itemImage;
    }
}
