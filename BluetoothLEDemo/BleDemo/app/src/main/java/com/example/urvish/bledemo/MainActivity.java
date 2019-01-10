package com.example.urvish.bledemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_FINE_PERMISSION = 2;
    private static final long
            SCAN_PERIOD =10000 ;
    private static final UUID SERVICE_UUID =UUID.fromString("24e43e2c-399b-4135-8e0d-a50e7baebe0c");
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Boolean mScanning = false;
    private static final int REQ_ENABLE_BLE = 1;
    private HashMap<String, BluetoothDevice> mScanResults;
    private static final String TAG = MainActivity.class.getSimpleName()+"this";
    private BleScanCallback mScanCallback;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler;
    private List<String> mAddress;
    private BluetoothGatt mGatt;
    private boolean mConnected=false;
    private Button mBtnStartScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeScanner=mBluetoothAdapter.getBluetoothLeScanner();
        mBtnStartScan=findViewById(R.id.startScan);

        mBtnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) finish();
    }

    private void startScan() {
        if (!hasPersmission() || mScanning) {
            return;
        }
        List<ScanFilter> scanFilters = new ArrayList<>();
        ScanFilter scanFilter=new ScanFilter.Builder().setServiceUuid(new ParcelUuid(SERVICE_UUID)).build();
        scanFilters.add(scanFilter);
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
        mScanResults = new HashMap<>();
        mAddress=new ArrayList<>();
        mScanCallback=new BleScanCallback(mScanResults);
        mBluetoothLeScanner.startScan(scanFilters,scanSettings,mScanCallback);
        mScanning=true;
        ((TextView)findViewById(R.id.textview)).setText("");
        mHandler=new Handler();
        mHandler.postDelayed(this::stopScan,SCAN_PERIOD);


    }
    private void stopScan(){
        if(mScanning && mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner!=null){
            mBluetoothLeScanner.stopScan(mScanCallback);
            scanComplete();
        }
        mScanCallback=null;
        mScanning=false;
        mHandler=null;
    }
    private void scanComplete(){
        if(mScanResults.isEmpty()){
            Toast.makeText(this, "No BLE device found please make sure device is discoverable", Toast.LENGTH_SHORT).show();
            return;
        }
        for(String deviceName:mScanResults.keySet()){
            Log.d(TAG, "scanComplete: Found"+deviceName);
            Toast.makeText(this, "Connected to device "+deviceName+"Wait for GATT to connect", Toast.LENGTH_LONG).show();
        }
    }

    private class BleScanCallback extends ScanCallback {
        public BleScanCallback(HashMap mScanResults) {
            super();
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            addScanResult(result);
        }



        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for(ScanResult result:results)
                addScanResult(result);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "onScanFailed: "+errorCode );
        }
    }
    private void addScanResult(ScanResult result) {
        BluetoothDevice device=result.getDevice();
        String address=device.getAddress();
        Log.d(TAG, "addScanResult: "+address);
        mAddress.add(address);
        mScanResults.put(address,device);
        stopScan();
        connectDevice(device);



    }

    private void connectDevice(BluetoothDevice device) {
        GattClientCallback gattClientCallback = new GattClientCallback();
        mGatt = device.connectGatt(this, false, gattClientCallback);
    }
    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(status!=BluetoothGatt.GATT_SUCCESS){
                disconnectGattServer();
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnected = true;
                Log.d(TAG, "onConnectionStateChange: "+newState);
                showstatus();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer();
            }
        }
    }

    private void showstatus() {
        if(mConnected){
            for(String address:mAddress){
                String name=mScanResults.get(address).getName();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((TextView)findViewById(R.id.textview)).append(name+" Connected!");
                        ((TextView)findViewById(R.id.textview)).setTextSize(50);
                    }
                });

            }
        }
    }

    private void disconnectGattServer() {
        mConnected=false;
        if(mGatt!=null){
            mGatt.disconnect();
            mGatt.close();
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


}
