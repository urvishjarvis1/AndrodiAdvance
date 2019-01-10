package com.example.urvish.lifecycleawarecomponantsdemo;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;

/**
 * LifeCycle Aware Componants:
 *  - MainActivity LifecycleOwner.
 *  - LifeCycleOwner interface has only one method getlifecycle which gives LifecycleObject.
 *  - Through Lifecycle object we can addobsever on owner.
 *  - The observer observes lifecycle states.
 *  - Handling onStop() event
 */
public class MainActivity extends AppCompatActivity implements LifecycleOwner,LifeCycleComponant {

    private static final String TAG =MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            
        }
        getLifecycle().addObserver(new MyObserver(MainActivity.this));

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        getState();

        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        getState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        getState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key","Value");
        Log.d(TAG, "onSaveInstanceState: Called");
    }

    @Override
    public void getState() {
        Log.d(TAG, "getState: "+getLifecycle().getCurrentState());
    }
}
