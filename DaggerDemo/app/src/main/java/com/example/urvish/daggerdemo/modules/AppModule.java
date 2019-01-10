package com.example.urvish.daggerdemo.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.urvish.daggerdemo.annotation.ContextName;
import com.example.urvish.daggerdemo.interfaces.RandomUserApi;
import com.example.urvish.daggerdemo.annotation.RandomUserApplicationScope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Reuseable will be reused when required
 */
@Module(includes = OkhttpClientModule.class)
public class AppModule {
    @Provides
    @Singleton
    public RandomUserApi randomUserApi(Retrofit retrofit){
        return retrofit.create(RandomUserApi.class);

    }


    @Provides
    @Singleton
    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory, Gson gson){
        return new Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build();

    }

    @Provides
    @Singleton
    public Gson gson(){
        GsonBuilder gsonBuilder=new GsonBuilder();
        return gsonBuilder.create();
    }
    @Provides
    @ContextName("Application")
    public Context context(Application context){ return context; }


    @Provides
    @Singleton
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
    @Singleton
    @Provides
    public Picasso picasso(@ContextName("Application") Context context, OkHttp3Downloader okHttp3Downloader){
        return new Picasso.Builder(context).downloader(okHttp3Downloader).build();
    }
    @Singleton
    @Provides
    public OkHttp3Downloader okHttp3Downloader(OkHttpClient okHttpClient){
        return new OkHttp3Downloader(okHttpClient);
    }
}
