package ru.mrcolt.anidubmobile.models;

public class MediaEpisodesListModel {

    private String title, URL, services;

    public MediaEpisodesListModel(String title, String URL, String services) {
        this.title = title;
        this.URL = URL;
        this.services = services;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

}
