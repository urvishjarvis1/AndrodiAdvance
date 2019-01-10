package com.example.urvish.replacingloaderdemo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class LocationViewModel extends AndroidViewModel {
    private  LocationLiveData mLocationLiveData;
    public LocationViewModel(@NonNull Application application) {
        super(application);
        mLocationLiveData=new LocationLiveData(application);
    }

    public LocationLiveData getmLocationLiveData() {
        return mLocationLiveData;
    }
}
