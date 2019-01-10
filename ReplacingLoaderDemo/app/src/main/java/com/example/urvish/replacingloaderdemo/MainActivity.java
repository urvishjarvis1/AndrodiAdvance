package com.example.urvish.replacingloaderdemo;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getSimpleName()+"this";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "You've to give permissions!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        LocationViewModel locationViewModel= ViewModelProviders.of(this).get(LocationViewModel.class);
        locationViewModel.getmLocationLiveData().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                ((TextView)findViewById(R.id.currentLocation)).setText("Latitude: "+location.getLatitude()+" Longitude "+location.getLongitude());
            }
        });
    }
}
