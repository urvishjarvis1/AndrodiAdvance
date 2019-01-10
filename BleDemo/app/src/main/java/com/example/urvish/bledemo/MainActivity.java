package com.example.urvish.bledemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG =MainActivity.class.getName() ;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private TextView mTxtViewBLEDevices;
    private boolean mConnected;
    private static final int BLUETOOTH_ENABLE_REQ=1;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
    private String LIST_NAME="list";
    private BluetoothLeService mBluetoothLeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtViewBLEDevices=findViewById(R.id.blueledevices);
        mTxtViewBLEDevices.setText(R.string.no_device);
        mTxtViewBLEDevices.setTextSize(20);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.bluetooth.le.ACTION_GATT_CONNECTED");
        intentFilter.addAction("com.example.bluetooth.le.ACTION_GATT_DISCONNECTED");
        intentFilter.addAction("com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED");
        intentFilter.addAction("com.example.bluetooth.le.ACTION_DATA_AVAILABLE");
        intentFilter.addAction("com.example.bluetooth.le.EXTRA_DATA");
        LocalBroadcastManager.getInstance(this).registerReceiver(mGattupdateReceiver,intentFilter);
        mBluetoothDeviceList=new ArrayList<>();
        mBluetoothManager=(BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter=mBluetoothManager.getAdapter();
        if(mBluetoothAdapter==null|| !mBluetoothAdapter.isEnabled()){
            Intent enableBLE=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBLE,BLUETOOTH_ENABLE_REQ);
        }
        mBluetoothAdapter.startLeScan(leListeners);
        Intent i=new Intent(this,BluetoothLeService.class);
        bindService(i,mConnetion,BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        if(mBluetoothAdapter!=null){
            mBluetoothAdapter.stopLeScan(leListeners);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattupdateReceiver);
        unbindService(mConnetion);
        super.onDestroy();
    }

    BluetoothAdapter.LeScanCallback leListeners=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                mBluetoothDeviceList.add(device);
                for(int i=0;i<mBluetoothDeviceList.size();i++)
                    mTxtViewBLEDevices.setText(mBluetoothDeviceList.get(i).getName());
        }
    };

    private BroadcastReceiver mGattupdateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                Log.d(TAG,"Connected:"+mConnected);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                Log.d(TAG,"Disconnected:"+mConnected);

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {


                } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

            }
        }
    };
    private boolean mBound;
    private ServiceConnection mConnetion=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"Service connection done!");
            BluetoothLeService.BluetoothLeBinder binder = (BluetoothLeService.BluetoothLeBinder) service;
            mBluetoothLeService =binder.getservice();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound=false;
            Log.d(TAG,"service disconnected");
        }
    };

}
