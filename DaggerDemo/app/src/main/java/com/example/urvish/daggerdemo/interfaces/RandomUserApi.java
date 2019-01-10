package com.example.urvish.daggerdemo.interfaces;

import com.example.urvish.daggerdemo.model.RandomUser;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit's iterface to fetch data from API using GET request.
 */
public interface RandomUserApi {
    @GET("api")
    Call<RandomUser> getRandomUser();
}
