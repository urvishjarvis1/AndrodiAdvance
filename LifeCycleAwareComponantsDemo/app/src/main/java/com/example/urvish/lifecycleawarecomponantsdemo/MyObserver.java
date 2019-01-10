package com.example.urvish.lifecycleawarecomponantsdemo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

/**
 * MyObserver:
 *  - Observes States of LifecycleOwner.
 *  - Implements LifecycleOwner:Marks Observer as LifecycleObserver.
 *  - relies on OnLifecycleEvent which provides annotation to monitor state of lifecycle.
 *
 */
public class MyObserver implements LifecycleObserver {
    private static String TAG=MainActivity.class.getSimpleName();
    private LifeCycleComponant lifeCycleComponant;
    private MainActivity mainActivity;


    public MyObserver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void myOnCreate(){
        Log.d(TAG, "myOncreate: ");
        lifeCycleComponant=mainActivity;
        lifeCycleComponant.getState();

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void myOnDestroy(){
        Log.d(TAG, "myOnDestroy: ");
        lifeCycleComponant.getState();
        lifeCycleComponant=null;

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void myOnResume(){
        Log.d(TAG, "myOnResume: ");
        lifeCycleComponant.getState();

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void myOnPause(){
        Log.d(TAG, "myOnPause: ");
        lifeCycleComponant.getState();


    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void myOnStop(){
        Log.d(TAG, "myOnStop: ");
        lifeCycleComponant.getState();
    }
}
