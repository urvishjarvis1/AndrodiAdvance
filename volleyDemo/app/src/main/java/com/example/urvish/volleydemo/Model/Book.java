package com.example.urvish.volleydemo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("volumeInfo")
    @Expose
    VolumeInfo volumeInfo;

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
