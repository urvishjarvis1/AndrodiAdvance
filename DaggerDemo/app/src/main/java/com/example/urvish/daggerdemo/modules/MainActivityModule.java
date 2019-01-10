package com.example.urvish.daggerdemo.modules;

import android.app.Activity;
import android.content.Context;

import com.example.urvish.daggerdemo.MainActivity;
import com.example.urvish.daggerdemo.annotation.ContextName;

import dagger.Module;
import dagger.Provides;
@Module
public class MainActivityModule {
    @Provides
    @ContextName("Activity")
    public Context providecontext(MainActivity context){ return context;}
}
