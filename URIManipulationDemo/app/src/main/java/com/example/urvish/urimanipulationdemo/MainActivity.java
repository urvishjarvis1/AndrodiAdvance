package com.example.urvish.urimanipulationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.txtview);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.2.158:3000/").build();
        URIService uriService=retrofit.create(URIService.class);
        uriService.loadName("https://www.google.com").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, ""+response.code(), Toast.LENGTH_SHORT).show();
                textView.append("first:"+response.message()+"\n");
                textView.append("first:"+response.body()+"\n");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        uriService.loadName("public/read").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, ""+response.code(), Toast.LENGTH_SHORT).show();
                textView.append("second:"+response.message()+"\n");
                textView.append("second:"+response.body()+"\n");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
