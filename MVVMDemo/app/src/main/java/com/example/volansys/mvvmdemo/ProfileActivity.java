package com.example.volansys.mvvmdemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.volansys.mvvmdemo.databinding.PhotoBinder;


public class ProfileActivity extends AppCompatActivity {
    private PhotoBinder profileBinding;
    private ProfileViewModel profileViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding=DataBindingUtil.setContentView(this,R.layout.activity_profile);
        Intent i=getIntent();
        profileViewModel=new ProfileViewModel(this,i);
        profileBinding.setPhotoData(profileViewModel);



    }
}
