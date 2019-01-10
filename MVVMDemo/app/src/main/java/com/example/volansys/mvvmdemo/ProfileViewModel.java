package com.example.volansys.mvvmdemo;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProfileViewModel extends BaseObservable {
    private LoginData loginData;
    private Context context;
    private Intent intent;
    private String profileImage;

    public ProfileViewModel(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        Bundle bundle;
        bundle=intent.getExtras();
        loginData=new LoginData();
        loginData.setEmail(bundle.getString("loginData"));
        loginData.setImgUrl("https://plus.google.com/u/2/photos/116155971378232332158/albums/profile/6501849024853657042?iso=false");

    }
    public String getProfileImage(){
        return loginData.getImgUrl();
    }
    @Bindable
    public String getDataEmail(){
        return loginData.getEmail();
    }
    @Bindable
    public String getDataNo(){
        return Integer.toString(CountingName.getCount(loginData.getEmail()));
    }
    @BindingAdapter({"profileImage"})
    public static void loadImage(ImageView view, String imageUrl) {
        view.setImageResource(R.drawable.ic_launcher_background);
    }
}
