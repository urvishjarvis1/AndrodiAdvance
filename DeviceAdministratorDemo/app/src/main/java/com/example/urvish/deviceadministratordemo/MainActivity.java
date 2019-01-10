package com.example.urvish.deviceadministratordemo;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="Admin" ;
    
    DevicePolicyManager devicePolicyManager;
    ComponentName mDevice;
    boolean isCamOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager=(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        Toast.makeText(this, "Device Owner:"+devicePolicyManager.isDeviceOwnerApp(getPackageName()), Toast.LENGTH_SHORT).show();
        Button btnDeviceLock=findViewById(R.id.buttondeviceforcelock);
        mDevice=ComponentName.createRelative(this,".DeviceAdminRec");
        isCamOff=devicePolicyManager.getCameraDisabled(mDevice);
        if(!devicePolicyManager.isAdminActive(mDevice)){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDevice);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION");
            startActivity(intent);

            /*Intent intent=new Intent().setComponent(new ComponentName("com.android.settings","com.android.settings.DeviceAdminSettings"));
            startActivity(intent);*/
        }
        btnDeviceLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.lockNow();

            }
        });
        Button btnWipeData=findViewById(R.id.buttonwipedata);
        btnWipeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlretDialog();

            }
        });
        final Button btnDisableCam=findViewById(R.id.buttondisablecamera);
        btnDisableCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCamOff){
                    devicePolicyManager.setCameraDisabled(mDevice,true);
                    isCamOff=devicePolicyManager.getCameraDisabled(mDevice);
                    Toast.makeText(MainActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button btnEnableCam=findViewById(R.id.buttonenable);
        btnEnableCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCamOff){
                    devicePolicyManager.setCameraDisabled(mDevice,false);
                    isCamOff=devicePolicyManager.getCameraDisabled(mDevice);
                    Toast.makeText(MainActivity.this, "Enabled", Toast.LENGTH_SHORT).show();

                }
            }
        });
        Button btnChangePass=findViewById(R.id.buttonchangepass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passwordChangeIntent = new Intent(
                        DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                passwordChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(passwordChangeIntent);
            }
        });
        Button btnEnaBleKiosk=findViewById(R.id.buttonkioskmode);
        btnEnaBleKiosk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] packages=new String[1];
                packages[0]="com.example.urvish.deviceadministratordemo";
                devicePolicyManager.isLockTaskPermitted(packages[0]);

                startLockTask();
            }
        });
        Button btnDisableKiosk=findViewById(R.id.buttondisablekiosk);
        btnDisableKiosk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLockTask();
            }
        });

    }

    private void createAlretDialog() {
        final AlertDialog.Builder alBuilder=new AlertDialog.Builder(this);
        alBuilder.setTitle("Are you sure").setMessage("Your data is about to be wiped");
        alBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                devicePolicyManager.wipeData(0);
                Toast.makeText(MainActivity.this, "Wipe data!", Toast.LENGTH_SHORT).show();
            }
        });
        alBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "NO!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog=alBuilder.create();
        alertDialog.show();
    }
}
