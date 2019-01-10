package com.example.urvish.androidservicediscovery;

import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.nsd.NsdManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity   {
    private static final String  SERVICE_NAME="Master Service";
    private static final String  SERVICE_TYPE="_http._tcp.";
    private NsdManager mNsdManager;
    private ServerSocket mServerSocket;
    private int mLocalPort;
    private boolean isOngoing=false;
    private NsdListener mNsdListener;
    private Button mBtnSendData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnSendData=findViewById(R.id.btnsendData);
        mBtnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("Hello From Server!");
            }
        });
        mNsdManager=(NsdManager)getSystemService(Context.NSD_SERVICE);
        try{
        mServerSocket=new ServerSocket(0);
        mLocalPort=mServerSocket.getLocalPort();}
        catch(IOException e){
            android.util.Log.d("Error","IOexception"+e.getLocalizedMessage());
        }


    }

    private void sendData(String s) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNsdManager!=null){
            registerService(mLocalPort);
        }
    }

    @Override
    protected void onPause() {
        if(mNsdManager!=null){
            mNsdManager.unregisterService(mNsdListener);
        }
        super.onPause();

    }



    public void registerService(int port){
        NsdServiceInfo nsdServiceInfo=new NsdServiceInfo();
        mNsdListener=new NsdListener();
        nsdServiceInfo.setPort(port);
        nsdServiceInfo.setServiceName(SERVICE_NAME);
        nsdServiceInfo.setServiceType(SERVICE_TYPE);
        mNsdManager.registerService(nsdServiceInfo,NsdManager.PROTOCOL_DNS_SD,mNsdListener);
    }
    private class NsdListener implements NsdManager.RegistrationListener{
        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.d("serviceName","Service Registration Failed"+errorCode);
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.d("serviceName","Service Unregistered : " + serviceInfo.getServiceName());
        }

        @Override
        public void onServiceRegistered(NsdServiceInfo serviceInfo) {
            String mServiceName=serviceInfo.getServiceName();
            Log.d("serviceName",mServiceName);
            Log.d("serviceName",""+serviceInfo.getPort());
            startServerSocket();
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
            Log.d("serviceName","Service Unregistered : " + serviceInfo.getServiceName());
        }
    }

    private void startServerSocket() {
        while (!isOngoing){
            try {
                Socket socket=mServerSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream());

                String stringData = input.readLine();
                Log.d("som", "startServerSocket: "+stringData);
                updateUI(stringData);
                output.println(stringData+"ack");
                output.flush();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateUI(String stringData) {
      //  ((TextView)findViewById(R.id.textview)).setText(stringData);
    }

}
