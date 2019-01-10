package com.example.volansys.attributesetters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.volansys.attributesetters.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static Context context;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        context=getApplicationContext();
        user=new User("Google","https://cdn.vox-cdn.com/thumbor/YodYS9ma7P_8jcAplZSoIlw4v-c=/0x0:2012x1341/920x613/filters:focal(0x0:2012x1341)/cdn.vox-cdn.com/uploads/chorus_image/image/47070706/google2.0.0.jpg");
        binding.setUser(user);
        binding.textView.setTextSize(50);
    }
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String imageUrl){
        if(imageUrl==null){
            Log.d("imageUrl","null");
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }else{
            Log.d("imageUrl","Not null");


            Glide.with(context).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                    Log.d("error","error"+model+isFirstResource);

                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                    Log.d("success!",model);
                    return false;
                }
            }).error(R.drawable.ic_launcher_foreground).into(imageView);
        }
    }



}
