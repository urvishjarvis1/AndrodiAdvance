package com.example.urvish.extendedlivedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.net.Network;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    int data=0;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.data);
        LiveData<Network> liveData=new MyLiveData(this);
        liveData.observe(this, new Observer<Network>() {
            @Override
            public void onChanged(@Nullable final Network network) {


                textView.setText(""+data++);



            }
        });
    }

}
