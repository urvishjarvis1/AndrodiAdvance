package com.example.volansys.advancedbinding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

import com.example.volansys.advancedbinding.databinding.ActivityMainBinding;
import com.example.volansys.advancedbinding.databinding.ViewStubBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);
        binding=ActivityMainBinding.inflate(getLayoutInflater());*/
       binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                ViewStubBinding stubBinding=DataBindingUtil.bind(inflated);
                User user=new User("urvish","rana");
                stubBinding.setUser(user);
            }
        });

    }
    public void inflateViewStub(View view) {
        if (!binding.viewStub.isInflated()) {
            binding.viewStub.getViewStub().inflate();
        }
    }
}
