package com.example.volansys.mvvmdemo;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.volansys.mvvmdemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        activity=this;
        MainViewModel mainViewModel=new MainViewModel(this);
        binding.setLoginData(mainViewModel);
        MyClickHandlers handlers=new MyClickHandlers(this);
        binding.setHandlers(handlers);

    }

}
