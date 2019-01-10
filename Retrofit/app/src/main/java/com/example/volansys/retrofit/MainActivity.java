package com.example.volansys.retrofit;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.volansys.retrofit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel=new MainViewModel(this,binding);
        Toast.makeText(this, "Please wait while Data Loading!", Toast.LENGTH_SHORT).show();
        mainViewModel.makeCall();
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "inOnclickView", Toast.LENGTH_SHORT).show();
                mainViewModel.submitDataToApi();
            }
        });

    }
}
