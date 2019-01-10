package com.example.urvish.nsdclientapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class sendMessage extends AsyncTask<String,Void,Void> {
    InetAddress mHostAddress;
    int port;
    public sendMessage(InetAddress address,int port) {
        mHostAddress=address;
        this.port=port;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try{
            Socket socke=new Socket(mHostAddress,port);
            OutputStream outputStream=socke.getOutputStream();
            outputStream.write(strings[0].getBytes());

            outputStream.close();
            socke.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }
}


