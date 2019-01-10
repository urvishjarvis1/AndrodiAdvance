package com.example.urvish.daggerdemo.modules;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class OkhttpClientModule {
    @Provides
    public OkHttpClient okHttpClient(){
        return new OkHttpClient().newBuilder().build();
    }
}
