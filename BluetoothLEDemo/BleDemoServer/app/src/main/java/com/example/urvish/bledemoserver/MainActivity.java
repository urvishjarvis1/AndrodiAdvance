package com.example.urvish.bledemoserver;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final UUID SERVICE_UUID = UUID.fromString("24e43e2c-399b-4135-8e0d-a50e7baebe0c");
    private static final String TAG = MainActivity.class.getSimpleName()+"this";
    private static final int REQ_FINE_PERMISSION =1 ;
    private static final int REQ_ENABLE_BLE = 2;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private GattServerCallback mGattServerCallback;
    private BluetoothGattServer mGattServer;
    private List<BluetoothDevice> mDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothManager=(BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter=mBluetoothManager.getAdapter();
        mDevices=new ArrayList<>();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBluetoothAdapter==null||!mBluetoothAdapter.isEnabled()){
            Intent enableBleIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBleIntent);
            finish();
            return;
        }
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            finish();
            return;
        }
        if(!mBluetoothAdapter.isMultipleAdvertisementSupported()){
            finish();
            return;
        }
        mBluetoothLeAdvertiser=mBluetoothAdapter.getBluetoothLeAdvertiser();
        mGattServerCallback=new GattServerCallback();
        mGattServer=mBluetoothManager.openGattServer(this,mGattServerCallback);
        setupServer();
        if(hasPersmission()){
            startAdvertising();
        }

    }
    private boolean hasPersmission() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            requestBluetoothEnable();
            return false;
        } else if (!hasLocationPermission()) {
            requestLocationPermission();
            return false;
        }
        return true;
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_FINE_PERMISSION);
        }else{
            Toast.makeText(this, "Give location permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasLocationPermission() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestBluetoothEnable() {
        Intent bluetoothEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bluetoothEnableIntent, REQ_ENABLE_BLE);
        Log.d(TAG, "requestBluetoothEnable: request for bluetooth");
    }

    private void startAdvertising() {
        ((TextView)findViewById(R.id.textView)).setText("Advertisement Started");
        if(mBluetoothLeAdvertiser==null){
            return;
        }
        AdvertiseSettings settings=new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTimeout(0).setConnectable(true)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW).build();
        ParcelUuid parcelUuid=new ParcelUuid(SERVICE_UUID);
        AdvertiseData data=new AdvertiseData.Builder().setIncludeDeviceName(true).
                addServiceUuid(parcelUuid).addManufacturerData(123,"data".getBytes()).build();
        mBluetoothLeAdvertiser.startAdvertising(settings,data,mAdvertiseCallback);
    }
    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d(TAG, "Peripheral advertising started.");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.d(TAG, "Peripheral advertising failed: " + errorCode);
        }
    };

    private void setupServer() {
        BluetoothGattService bluetoothGattService=new BluetoothGattService(SERVICE_UUID,BluetoothGattService.SERVICE_TYPE_PRIMARY);
        mGattServer.addService(bluetoothGattService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAdvertising();
        stopSever();
    }

    private void stopSever() {
        if(mGattServer!=null){
            mGattServer.close();
        }
    }

    private void stopAdvertising() {
        if(mBluetoothLeAdvertiser!=null){
            ((TextView)findViewById(R.id.textView)).setText("Advertisement Stopped");
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
    }

    private class GattServerCallback extends BluetoothGattServerCallback {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mDevices.add(device);
                Log.d(TAG, "onConnectionStateChange: Device "+ device.getName()+" "+ device.getAddress() +"added");

                //Toast.makeText(MainActivity.this, "Added:"+device.getName(), Toast.LENGTH_SHORT).show();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mDevices.remove(device);
            }
        }
    }


}
