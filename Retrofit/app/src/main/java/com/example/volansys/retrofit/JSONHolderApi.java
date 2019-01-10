package com.example.volansys.retrofit;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONHolderApi {
    //to do here...
    @GET("read")
     Call<List<User>> getUserwithId();

    /*@FormUrlEncoded*/
    /*@POST("/postData")
    public Call<ResponseBody> postUser(@Field("name") String name
                                        @);*/


}
