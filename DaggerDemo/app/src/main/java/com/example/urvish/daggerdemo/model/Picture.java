package com.example.urvish.daggerdemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Picture {
    @SerializedName("large")
    @Expose
    String large;
    @SerializedName("medium")
    @Expose
    String medium;
    @SerializedName("thumbnail")
    @Expose
    String thumbnail;

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
