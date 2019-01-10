package com.example.urvish.fingerprintauthenticationdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context mContext;
    public FingerprintHandler(MainActivity mainActivity) {
        this.mContext=mainActivity;
    }
    public void startAuth(FingerprintManager manager,FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal=new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        manager.authenticate(cryptoObject,cancellationSignal,0,this,null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("FingerprintAuthentication error\n"+errString,false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("FingerprintAuthentication help\n"+helpString,false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Authentication successfull",true);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authentication Failed",false);
    }
     private void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)mContext).findViewById(R.id.errorText);
        textView.setText(e);
        if(success){
            textView.setTextColor(Color.GREEN);
            Toast.makeText(mContext, "Authentication successful", Toast.LENGTH_SHORT).show();


        }
    }
}
