package com.example.urvish.urimanipulationdemo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface URIService {
    @GET
    Call<ResponseBody> loadName(@Url String url);

}
