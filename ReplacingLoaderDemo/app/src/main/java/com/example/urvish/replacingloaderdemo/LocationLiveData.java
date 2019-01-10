package com.example.urvish.replacingloaderdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationLiveData extends LiveData<Location> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG="MainActivitythis";
    private Context context;
    //this is the sol I found
    private LocationRequest  mLocationRequest = LocationRequest.create()
            .setInterval(10000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setFastestInterval(1000)
            .setSmallestDisplacement(1);
    private LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: ");
            postValue(location);
        }
    };

    private GoogleApiClient googleApiClient;

    public LocationLiveData(Context context) {
        this.context=context;
        googleApiClient = new GoogleApiClient.Builder(context, this, this).addApi(LocationServices.API).build();
    }

    //Live Data Methods
    @Override
    protected void onActive() {
        super.onActive();
        googleApiClient.connect();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,locationListener);
        }
        googleApiClient.disconnect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("MissingPermission")
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null){
            setValue(location);
        }
        if(hasActiveObservers()){
            Log.d(TAG, "onConnected: "+hasActiveObservers());
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,/*this is where i stuck*/mLocationRequest,locationListener);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }


}
