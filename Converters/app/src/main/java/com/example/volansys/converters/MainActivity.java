package com.example.volansys.converters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.volansys.converters.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        Item itm=new Item();
        itm.setDose(100);
        itm.setDate(new Date());
        binding.setItem(itm);
        binding.first.append("");
        binding.second.append("");
    }
}
