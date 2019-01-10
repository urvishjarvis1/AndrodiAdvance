package com.example.urvish.bluetoothmeshdemo;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Piconet {
    //TAG
    private static String TAG= Piconet.class.getName();
    //Name of protocol
    private static String PICONET="ANDROID_PICONET_BLUETOOTH";
    //Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter;
    //HashMap for connection
    //map of socket related to address.
    private HashMap<String,BluetoothSocket> mBluetoothSocket;
    //Thread for connection
    private HashMap<String,Thread> mBluetoothConncetionThread;
    //UUID of device
    private ArrayList<UUID> mUUIDList;
    //Address of bluetooth devices
    private ArrayList<String> mBluetoothDeviceAddress;
    //context
    private static Context context;
    //ServerSocket
    private BluetoothServerSocket mServerSocket;


    private static Handler handler=new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case 1:
                  Toast.makeText(context, msg.getData().getString("Data"), Toast.LENGTH_SHORT).show();
                  break;
                  default:
                      break;
          }
      }
    };

    public Piconet(Context context) {
        this.context = context;
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        mBluetoothSocket=new HashMap<>();
        mBluetoothConncetionThread=new HashMap<>();
        mUUIDList=new ArrayList<>();
        mBluetoothDeviceAddress=new ArrayList<>();
        mUUIDList.add(UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
        mUUIDList.add(UUID.fromString("54d1cc90-1169-11e2-892e-0800200c9a66"));
        mUUIDList.add(UUID.fromString("6acffcb0-1169-11e2-892e-0800200c9a66"));
        mUUIDList.add(UUID.fromString("7b977d20-1169-11e2-892e-0800200c9a66"));
        mUUIDList.add(UUID.fromString("815473d0-1169-11e2-892e-0800200c9a66"));
        mUUIDList.add(UUID.fromString("503c7434-bc23-11de-8a39-0800200c9a66"));
        mUUIDList.add(UUID.fromString("503c7435-bc23-11de-8a39-0800200c9a66"));

        Thread connectionProvider=new Thread(new ConnectionProvider());
        connectionProvider.start();

    }


    public void startPicoNet(){
        Log.d(TAG,"Started Discovery");
        Set<BluetoothDevice> pairedDevice=mBluetoothAdapter.getBondedDevices();
        if(pairedDevice.size()>0){
            for(BluetoothDevice device:pairedDevice){
                BluetoothDevice remoteDevice=mBluetoothAdapter.getRemoteDevice(device.getAddress());
                connect(remoteDevice);
            }
        }else{
            Toast.makeText(context, "No Paired Device Found", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectionProvider implements Runnable{

        @Override
        public void run() {
            try{
                for(int i=0;i<mUUIDList.size();i++){
                     mServerSocket=mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PICONET,mUUIDList.get(i));
                    Log.d(TAG,"Server Socket open for"+i+"Device(s)");
                    Log.d(TAG,"Waiting for client");
                    BluetoothSocket mSocket=mServerSocket.accept();
                    Log.d(TAG,"Accepted For "+i+" device(s)");
                    try{
                        mServerSocket.close();
                    }catch (IOException e){
                        Log.e(TAG,"Closing of Server socket failed"+e.getLocalizedMessage());
                    }
                    if(mSocket!=null){
                        String address=mSocket.getRemoteDevice().getAddress();
                        mBluetoothSocket.put(address,mSocket);

                        mBluetoothDeviceAddress.add(address);
                        Thread mBluetoothConnectionThread =new Thread(new BluetoothConnection(mSocket));
                        mBluetoothConnectionThread.start();
                        Log.d(TAG,"Adding address "+address+"into hashMap");
                        mBluetoothConncetionThread.put(address,mBluetoothConnectionThread);
                    }else{
                        Log.e(TAG,"not able to connect to device");
                    }
                }
            }catch(IOException e){
                Log.e(TAG,"cannot establish connection"+e.getLocalizedMessage());
            }
        }
    }
    private class BluetoothConnection implements Runnable{
        private String address;
        private final InputStream inputStream;

        public BluetoothConnection(BluetoothSocket mSocket) {
            InputStream is=null;
            try{
                is=new DataInputStream(mSocket.getInputStream());
            }catch (IOException e){
                Log.e(TAG,"Not able to open input stream form socket"+e.getLocalizedMessage());
            }
            inputStream=is;
        }

        @Override
        public void run() {
            byte[] buffer=new byte[1024];
            String msg="";
            int readByte;
                try{
                    while ((readByte=inputStream.read())!=-1) {

                        if (readByte == -1) {
                            Log.e(TAG, "Discarding msg" + msg);
                            msg = "";

                        }
                        buffer[0] = (byte) readByte;
                        if (readByte == 0) {
                            onReceiveMsg(msg);
                            msg = "";
                        } else {
                            msg += new String(buffer, 0, 1);
                            /*onReceiveMsg(msg);*/
                        }
                    }
                }catch (IOException e){
                    Log.e(TAG,"Disconnected");

                }
                /*mBluetoothDeviceAddress.remove(address);
                mBluetoothSocket.remove(address);
                mBluetoothConncetionThread.remove(address);*/

        }
    }

    private void onReceiveMsg(String msg) {
        if(msg!=null && msg.length()>0){
            Log.i(TAG,msg);
            Bundle bundle=new Bundle();
            bundle.putString("Data",msg);
            Message message=new Message();
            message.what=1;
            message.setData(bundle);
            handler.sendMessage(message);
            Log.d(TAG, "onReceiveMsg: MessageReceived");
        }
    }
    private BluetoothSocket getConnectedSocket(BluetoothDevice device,UUID uuid){
        BluetoothSocket bluetoothSocket;
        try{
            bluetoothSocket=device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            return bluetoothSocket;
        }catch (Exception e){
            Log.e(TAG,"Couldn't get  client Socket: "+e.getLocalizedMessage());
        }
        return null;
    }
    private void connect(BluetoothDevice device){
        BluetoothSocket mSocket=null;
        String address=device.getAddress();
        BluetoothDevice remoteDevice=mBluetoothAdapter.getRemoteDevice(address);
        for(int i=0;i<mUUIDList.size()&&mSocket==null;i++){
            for (int j=0;j<2&&mSocket==null;j++){
                Log.d(TAG,"Trying to connect... "+j+"times with"+remoteDevice.getName());
                mSocket = getConnectedSocket(remoteDevice, mUUIDList.get(i));

            }

        }
        if (mSocket==null){
            Log.e(TAG,"not connected ");
            return;
        }
        Log.d(TAG,"Connected with "+device.getName());
        mBluetoothSocket.put(address,mSocket);
        mBluetoothDeviceAddress.add(address);
        Thread mConnectionThread=new Thread(new BluetoothConnection(mSocket));
        mConnectionThread.start();
        mBluetoothConncetionThread.put(address,mConnectionThread);

    }
    public void broadcastMessage(String msg){
        for (int i = 0; i < mBluetoothDeviceAddress.size(); i++) {
            sendMessage(mBluetoothDeviceAddress.get(i),msg);
        }
    }
    public void sendMessage(String add,String msg){
        BluetoothSocket mSocket=mBluetoothSocket.get(add);
        if(mSocket!=null){
            try {
                OutputStream outStream = mSocket.getOutputStream();
                final int pieceSize = 16;
                for (int i = 0; i < msg.length(); i += pieceSize) {
                    byte[] send = msg.substring(i,
                            Math.min(msg.length(), i + pieceSize)).getBytes();
                    outStream.write(send);
                    Log.d(TAG, "sendMessage: Message sent!");
                }
                byte[] terminateFlag = new byte[1];
                terminateFlag[0] = 0; // ascii table value NULL (code 0)
                outStream.write(new byte[1]);
            } catch (IOException e) {
                Log.d(TAG, "line 236", e);
                e.printStackTrace();
            }
        }

    }
    void serverClose(){
        if(mServerSocket!=null){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG,"Not able to close server socket"+e.getLocalizedMessage());
            }
        }
    }

}
