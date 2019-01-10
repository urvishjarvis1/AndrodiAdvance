package com.example.urvish.daggerdemo.componant;

import android.app.Application;
import android.content.Context;

import com.example.urvish.daggerdemo.MyApplication;

import com.example.urvish.daggerdemo.modules.ActivityBuilderModule;

import com.example.urvish.daggerdemo.modules.AppModule;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Central point where it'll communicate to Modules and Classes.
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class,ActivityBuilderModule.class,AppModule.class})
public interface AppComponant {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponant build();
    }
    void inject(MyApplication application);

}
