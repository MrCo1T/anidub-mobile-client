package ru.mrcolt.anidub.models;

public class MediaSearchListModel {

    private String poster, titleRU, titleEN, rating, year, genre, country, episode, description, newsID;

    public MediaSearchListModel(String poster, String titleRU, String titleEN, String rating, String year,
                                String genre, String country, String episode, String description,
                                String newsID) {
        this.poster = poster;
        this.titleRU = titleRU;
        this.titleEN = titleEN;
        this.rating = rating;
        this.year = year;
        this.genre = genre;
        this.country = country;
        this.episode = episode;
        this.description = description;
        this.newsID = newsID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitleRU() {
        return titleRU;
    }

    public void setTitleRU(String titleRU) {
        this.titleRU = titleRU;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

}