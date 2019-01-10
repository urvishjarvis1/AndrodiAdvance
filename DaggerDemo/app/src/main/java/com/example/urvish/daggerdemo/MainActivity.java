package com.example.urvish.daggerdemo;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.urvish.daggerdemo.annotation.ContextName;
import com.example.urvish.daggerdemo.interfaces.RandomUserApi;
import com.example.urvish.daggerdemo.model.RandomUser;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MainActivity
 *  - RandomUserCompanant - componant which uses modules to give Dependencies to class
 *  - Annotation added
 */

public class MainActivity extends AppCompatActivity {
    private TextView mTextviewName,mTextviewAdd,mTextviewemail;
    @Inject
     RandomUserApi randomUserApi;

    private ImageButton mbtnRefresh;
    @Inject
     Picasso picasso;

     ImageView imageView;


    @Inject
    @ContextName("Application")
    Context applicationContext;
    @Inject
    @ContextName("Activity")
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidInjection.inject(this);
        Toast.makeText(context, "Toast using Qualifier Annotation object", Toast.LENGTH_SHORT).show();
        initViews();
        callApi();
    }
    @RequiresPermission(Manifest.permission.INTERNET)
    private void callApi() {
        Call<RandomUser> randomUserCall=getRandomUserService().getRandomUser();
        randomUserCall.enqueue(new Callback<RandomUser>() {
            @Override
            public void onResponse(@NonNull Call<RandomUser> call, @NonNull Response<RandomUser> response) {
               RandomUser randomUser=response.body();
                mTextviewName.setText(randomUser.getResults().get(0).getName().getFirst());
                mTextviewAdd.setText(randomUser.getResults().get(0).getLocation().getCity());
                mTextviewemail.setText(randomUser.getResults().get(0).getEmail());
                picasso.load(randomUser.getResults().get(0).getPicture().getLarge()).resize(500,500).into(imageView);
                Log.d("Dagger", "onResponse: respons:"+response.body());

            }

            @Override
            public void onFailure(Call<RandomUser> call, Throwable t) {
                Log.d("Dagger", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

    private void initViews() {
        mTextviewAdd=findViewById(R.id.address);
        mTextviewemail=findViewById(R.id.email);
        mTextviewName=findViewById(R.id.name);
        mbtnRefresh=findViewById(R.id.refresh);

        imageView=findViewById(R.id.profileImg);
        mbtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applicationContext==getApplicationContext())
                    Toast.makeText(applicationContext, "Testing using Application context", Toast.LENGTH_LONG).show();
                initViews();callApi();
            }
        });
    }



    public RandomUserApi getRandomUserService(){
        return randomUserApi;
    }
}
