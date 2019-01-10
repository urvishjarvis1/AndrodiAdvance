package com.example.urvish.lazyinjection;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Module for providing Integer.
 */
@Module
public class CountModule {
    int num;
    @Provides
    Context getApplicationContext(Application application){
        return application.getApplicationContext();
    }
    @Provides
    Integer providesInteger(){
        return num++;
    }
}
