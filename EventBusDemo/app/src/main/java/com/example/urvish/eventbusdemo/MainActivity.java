package com.example.urvish.eventbusdemo;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private FirstFragment mFirstFragment;
    private SecondFragment mSecondFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirstFragment=new FirstFragment();
        mSecondFragment=new SecondFragment();
        manager=getSupportFragmentManager();
        fragmentTransaction=manager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content,mFirstFragment);
        fragmentTransaction.commit();
        EventBus.getDefault().register(this);


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyEvent notifyEvent){
        Toast.makeText(this, "MainActivity"+notifyEvent.mValue, Toast.LENGTH_SHORT).show();
        fragmentTransaction=manager.beginTransaction();
        fragmentTransaction.replace(R.id.firstfragment,mSecondFragment);
        fragmentTransaction.commit();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
