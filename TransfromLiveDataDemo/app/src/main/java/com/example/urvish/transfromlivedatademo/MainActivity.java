package com.example.urvish.transfromlivedatademo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.net.Network;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName()+"this" ;
    private static int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LiveData<Network> networkData = new ConnectivityLiveData(this);

        LiveData<Integer> userNames = android.arch.lifecycle.Transformations.map(networkData, Interger ->
                {
                    int data=networkData.getValue().describeContents()+id;
                    id++;
                    return data;
                }
        );
        userNames.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "onChanged: "+integer);

                ((TextView) findViewById(R.id.textview)).setText("" + integer);
            }
        });

    }
}
