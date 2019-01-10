package com.example.urvish.mediaterlivedataexample;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;


public class MyViewModel extends ViewModel {
    private MutableLiveData<String> firstname;
    private MutableLiveData<String> lastname;
    private static final String TAG=MyViewModel.class.getSimpleName();

    public MyViewModel() {
        super();
        Log.d(TAG, "MyViewModel: started");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: called & "+TAG+" Stopped");
    }

    public MutableLiveData<String> getFirstname() {
        if (firstname==null)
            firstname=new MutableLiveData<String>();
        return firstname;
    }

    public MutableLiveData<String> getLastname() {
        if (lastname==null)
            lastname=new MutableLiveData<>();
        return lastname;
    }

}
