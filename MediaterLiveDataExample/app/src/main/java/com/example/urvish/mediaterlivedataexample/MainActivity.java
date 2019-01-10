package com.example.urvish.mediaterlivedataexample;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyViewModel myViewModel;
    private Button buttonfname,buttonSname;
    private static int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSname=findViewById(R.id.btn2);
        buttonSname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id%2==0) {

                    //myViewModel.getLastname().setValue("Urvish");
                    myViewModel.getLastname().setValue("Rana");
                    id++;
                }else{
                    //myViewModel.getLastname().setValue("Jarvis");
                    myViewModel.getLastname().setValue("Stark");
                    id++;
                }
            }
        });
        buttonfname=findViewById(R.id.btn);
        buttonfname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id%2==0) {

                    myViewModel.getLastname().setValue("Urvish");
                    //myViewModel.getLastname().setValue("Rana");
                    id++;
                }else{
                    myViewModel.getLastname().setValue("Jarvis");
                    //myViewModel.getLastname().setValue("Stark");
                    id++;
                }
            }
        });
        myViewModel= ViewModelProviders.of(this).get(MyViewModel.class);
        final MediatorLiveData<String> mediatorLiveData=new MediatorLiveData<>();
        mediatorLiveData.addSource(myViewModel.getFirstname(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mediatorLiveData.setValue(s);
            }
        });
        mediatorLiveData.addSource(myViewModel.getLastname(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mediatorLiveData.setValue(s);
            }
        });
        mediatorLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ((TextView)findViewById(R.id.text)).setText(s);
            }
        });
    }
}

