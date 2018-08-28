package ru.mrcolt.anidubmoblie;

import java.io.Serializable;

import javax.security.auth.Destroyable;

public class AnimeDataModel {

    public String poster, title, year, country, rating, genre, description, newsID;

    public AnimeDataModel(String poster, String title, String year, String country, String rating, String genre, String description, String newsID) {
        this.poster = poster;
        this.title = title;
        this.year = year;
        this.country = country;
        this.rating = rating;
        this.genre = genre;
        this.description = description;
        this.newsID = newsID;
    }


    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTitle(String title) { this.title = title; }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCountry(String country) { this.country = country; }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNewsID(String newsID) { this.newsID = newsID; }

    public String getPoster() { return poster; }

    public String getTitle() { return title; }

    public String getYear() { return year; }

    public String getCountry() { return country; }

    public String getRating() {
        return rating;
    }

    public String getGenre() { return genre; }

    public String getDescription() { return description; }

    public String getNewsID() { return newsID; }
}
