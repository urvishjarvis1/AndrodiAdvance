package com.example.urvish.bluetoothmeshdemo;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtnDiscovery,mBtnSendBroadcast;
    private Piconet piconet;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        piconet.serverClose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        piconet=new Piconet(this);
        mBtnDiscovery=findViewById(R.id.btndiscovery);
        mBtnSendBroadcast=findViewById(R.id.btnSendBroadcast);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null||!mBluetoothAdapter.isEnabled()){
            Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,1);
        }
        mBtnDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             piconet.startPicoNet();
            }
        });
        mBtnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piconet.broadcastMessage("Hello world! form:"+mBluetoothAdapter.getName());
            }
        });
    }
}
