package com.example.volansys.mvvmdemo;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainViewModel extends BaseObservable {
    private LoginData loginData;
    private Context context;

    public MainViewModel(Context context) {
        this.context = context;
        this.loginData=new LoginData();

    }
    @Bindable
    public String getDataEmail(){
        return loginData.getEmail();
    }
    @Bindable
    public String getDataPassword(){
        return loginData.getPassword();
    }

    public void setDataEmail(String email){
        loginData.setEmail(email);
        notifyPropertyChanged(BR.dataEmail);
    }
    public void setDataPassword(String pass){
        loginData.setPassword(pass);
    }
    public View.OnClickListener onLoginButtonClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Login ", getDataEmail() + getDataPassword());
                Toast.makeText(view.getContext(), "Login username:" + getDataEmail() +" Password:"+ getDataPassword(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context.getApplicationContext(),ProfileActivity.class);
                String name=loginData.getEmail();
                intent.putExtra("loginData",name);//todo here!
                context.startActivity(intent);
            }
        };
    }

}
