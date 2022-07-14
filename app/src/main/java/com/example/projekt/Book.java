package com.example.projekt;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Book implements Serializable {

    @Exclude private String id;
    private String byUser;

    private String title;
    private String author;
    private String numberOfPages;

    public Book(String title, String author, String numberOfPages, String byUser) {
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.byUser=byUser;
    }

    public Book(String title, String author, String numberOfPages) {
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
    }

    public Book(){

    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}
