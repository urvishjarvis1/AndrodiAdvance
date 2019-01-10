package com.example.volansys.attributesetters;

public class User {
    private String name;
    private String profileUrl;

    public User(String name, String profileUrl) {
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
