package com.example.urvish.volleydemo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VolumeInfo {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("publisher")
    @Expose
    private String publishername;
    @SerializedName("publishedDate")
    @Expose
    private String publishedDate;
    @SerializedName("authors")
    @Expose
    private List<String> authors;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishername() {
        return publishername;
    }

    public void setPublishername(String publishername) {
        this.publishername = publishername;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
}
