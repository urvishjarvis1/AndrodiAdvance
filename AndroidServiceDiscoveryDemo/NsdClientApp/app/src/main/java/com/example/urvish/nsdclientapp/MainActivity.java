package com.example.urvish.nsdclientapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.support.annotation.BinderThread;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity {
    private static String SERVICE_NAME="Client_Device";
    private static String SERVICE_TYPE="_http._tcp.";
    public static InetAddress mHostAddress;
    public static int mHostPort;
    private NsdManager mNsdManager;
    private sendMessage message;
    private DiscoveryListner mDiscoveryListner;
    private Button mBtnSendData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDiscoveryListner=new DiscoveryListner();
        mBtnSendData=findViewById(R.id.btnsendData);
        mBtnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("Hello from Client!");
            }
        });
        mNsdManager=(NsdManager)getSystemService(Context.NSD_SERVICE);

    }

    private void sendData(final String s) {
        new sendMessage(mHostAddress,mHostPort).execute(s);
    }

    private void updateUI(String data) {
        ((TextView)findViewById(R.id.textview)).setText(data);
    }

    @Override
    protected void onPause() {
        if (mNsdManager!=null){
            mNsdManager.stopServiceDiscovery(mDiscoveryListner);

        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNsdManager!=null){
            mNsdManager.discoverServices(SERVICE_TYPE,NsdManager.PROTOCOL_DNS_SD,mDiscoveryListner);
        }
    }



    private class DiscoveryListner implements NsdManager.DiscoveryListener{

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.d("Service","Discovery Start Failed:"+errorCode);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.d("Service","Discovery Stop Failed:"+errorCode);
        }

        @Override
        public void onDiscoveryStarted(String serviceType) {
            Log.d("Service","Discovery Started");
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.d("Service","Discovery Stopped");
        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            Log.d("Service","Service Found:\n   Name:"+serviceInfo.getServiceName()+"\n     Port:"+serviceInfo.getPort());
            if(!serviceInfo.getServiceType().equals(SERVICE_TYPE)){
                Log.d("Service","Service not recognized");
            }else{
                Log.d("Service","Host ip"+serviceInfo.getPort());
                mNsdManager.resolveService(serviceInfo,new ResloverListener());
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            Log.d("Service","Service Lost");
        }
    }
    private class ResloverListener implements NsdManager.ResolveListener{

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.d("Service","Service Resolution error"+errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.d("Service","Service Resolvled Success"+serviceInfo.getServiceName());
            mHostAddress=serviceInfo.getHost();
            mHostPort=serviceInfo.getPort();
            message=new sendMessage(mHostAddress,mHostPort);
            Toast.makeText(MainActivity.this, "Connected to service:Initiate Message", Toast.LENGTH_SHORT).show();
            Log.d("Service","Service Name:"+serviceInfo.getServiceName()+"\nPort:"+serviceInfo.getPort()+"\nHost IP:"+serviceInfo.getHost().toString());
        }
    }


}
