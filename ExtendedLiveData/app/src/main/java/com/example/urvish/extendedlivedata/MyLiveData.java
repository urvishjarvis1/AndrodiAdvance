package com.example.urvish.extendedlivedata;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest;

public class MyLiveData extends LiveData<Network> {
    private ConnectivityManager mConnectivityManager;
    private NetworkCallback networkCallback=new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(Network network) {
            postValue(network);
        }

        @Override
        public void onLost(Network network) {
            postValue(network);
        }
    };

    public MyLiveData(Context context) {
        mConnectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onActive() {
        super.onActive();
        mConnectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mConnectivityManager.unregisterNetworkCallback(networkCallback);
    }
}
