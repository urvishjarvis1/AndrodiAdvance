package com.example.urvish.glidepicassodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView,mImageViewPicasso;
    private Spinner mSpinner;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        Glide.with(this)
                .load("https://randomuser.me/api/portraits/med/women/54.jpg")
                .into(mImageView);

        Picasso.get().load("https://randomuser.me/api/portraits/med/women/54.jpg").resize(500,500).centerCrop().into(mImageViewPicasso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        mImageView=findViewById(R.id.imageview);
        mButton=findViewById(R.id.btnloadimg);
        mImageViewPicasso=findViewById(R.id.picassoImageview);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlideApp.with(MainActivity.this)
                        .load("https://randomuser.me/api/portraits/women/54.jpg")
                        .centerCrop().into(mImageView);
            }
        });
    }


}
