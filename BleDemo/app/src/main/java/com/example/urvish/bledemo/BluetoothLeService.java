package com.example.urvish.bledemo;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;

public class BluetoothLeService extends Service {
    private IBinder mBinder=new BluetoothLeBinder();
    private static final String TAG =BluetoothLeService.class.getName();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public BluetoothLeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }
    private final BluetoothGattCallback mBluetoothGattCallback=new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if(newState== BluetoothProfile.STATE_CONNECTED){
                intentAction=ACTION_GATT_CONNECTED;
                mConnectionState=STATE_CONNECTED;
                broadcastIntent(intentAction);
                Log.i(TAG,"Connected to GATT server");
                Log.i(TAG,"Starting Discovery"+mBluetoothGatt.discoverServices());
            }else if(newState==BluetoothProfile.STATE_DISCONNECTED){
                intentAction=ACTION_GATT_DISCONNECTED;
                mConnectionState=STATE_DISCONNECTED;
                broadcastIntent(intentAction);
                Log.i(TAG,"Disconnected from GATT server");

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if(status==BluetoothGatt.GATT_SUCCESS){
                broadcastIntent(ACTION_GATT_SERVICES_DISCOVERED);
            }else{
                Log.e(TAG,"onServiceDiscovered received:"+status);
            }


        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status==BluetoothGatt.GATT_SUCCESS){
                broadcastIntent(ACTION_DATA_AVAILABLE,characteristic);
            }
        }
    };

    private void broadcastIntent(String intentAction) {
        Intent intent=new Intent(intentAction);
        sendBroadcast(intent);
    }

    private void broadcastIntent(String intentAction,BluetoothGattCharacteristic characteristic) {
        Intent intent=new Intent(intentAction);
        byte[] data=characteristic.getValue();
        if(data!=null||data.length>0){
            StringBuilder stringBuilder=new StringBuilder(data.length);
            for(byte bytechar:data){
                stringBuilder.append(String.format("%02X",bytechar));
            }
            intent.putExtra("EXTRA_DATA",new String(data)+"\n"+stringBuilder.toString());
        }
        sendBroadcast(intent);

    }
    public class BluetoothLeBinder extends Binder{
        BluetoothLeService getservice(){
            return BluetoothLeService.this;
        }
    }
}
