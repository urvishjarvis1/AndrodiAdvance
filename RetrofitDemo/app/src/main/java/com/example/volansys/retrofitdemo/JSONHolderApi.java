package com.example.volansys.retrofitdemo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONHolderApi {
    //to do here...
    @GET("read")
     Call<List<User>> getUserwithId();

    @Headers({
            "Accept: application/vnd.yourapi.v1.full+json",
            "User-Agent: RetrofitDemo"
    })
    @POST("create")
     Call<ResponseBody> postUser(@Body User user);


    @FormUrlEncoded
    @PUT("update")
     Call<ResponseBody> updateUser(@Field("name") String name,@Field("email") String email,@Field("password")String pass);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "delete",hasBody = true)
    Call<ResponseBody> deleteUser(@Field("name") String name);

}
