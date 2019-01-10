package com.example.volansys.retrofitdemo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtility {
    private static NetworkUtility sNetworkUtility;
    private Retrofit mRetrofit;
    public static final String BASE_URL="http://192.168.2.158:3000/public/";
    public static NetworkUtility getNetworkUtility() {
        if(sNetworkUtility==null){
            sNetworkUtility=new NetworkUtility();
        }
        return sNetworkUtility;

    }
    public JSONHolderApi jsonHolderApi(){
        return mRetrofit.create(JSONHolderApi.class);
    }
    private NetworkUtility(){
        mRetrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
