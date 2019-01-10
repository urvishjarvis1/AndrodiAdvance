package com.example.urvish.sharedprefrencelogin.fragments;

/**
 * Created by urvish on 31/1/18.
 */

public interface FragmentCommunicator {
    public void sendData(String uname,String pass,int pin);
    public void sendData(int PIN);

}
