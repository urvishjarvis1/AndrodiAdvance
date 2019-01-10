package com.example.urvish.lazyinjection;

import android.util.Log;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Lazy injection which will create Object lazily once get() method called.
 *
 */
public class LazyCounter {
    private String TAG="results";
    @Inject
    Lazy<Integer> lazy;
    @Inject
    public LazyCounter(){

    }

    public Integer  print(){
        System.out.println("printing...");
        return lazy.get();
    }
}
