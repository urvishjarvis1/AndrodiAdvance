package com.example.urvish.livedatademo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> mName;

    public MutableLiveData<String> getmName() {
        if(mName==null)
            mName=new MutableLiveData<>();
        return mName;
    }
}
