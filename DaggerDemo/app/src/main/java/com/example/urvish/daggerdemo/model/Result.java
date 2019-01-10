package com.example.urvish.daggerdemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("name")
    @Expose
    Name name;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private Picture picture;

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
