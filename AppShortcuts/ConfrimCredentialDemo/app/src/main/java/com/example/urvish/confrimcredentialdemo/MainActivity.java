package com.example.urvish.confrimcredentialdemo;

import android.app.KeyguardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private KeyguardManager mKeyguardManager;
    private static final int KEY_GUARD_REQ_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKeyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        assert mKeyguardManager != null;
        if(!mKeyguardManager.isDeviceSecure()){
            Toast.makeText(this, "this device's lock screen doesn't set up!", Toast.LENGTH_SHORT).show();
        }

        Intent intent=mKeyguardManager.createConfirmDeviceCredentialIntent(null,null);
        if(intent!=null){
            startActivityForResult(intent,KEY_GUARD_REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==KEY_GUARD_REQ_CODE){
            if(resultCode==RESULT_OK){
                ((TextView)findViewById(R.id.textview)).setText(R.string.granted);

            }else{
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                ((TextView)findViewById(R.id.textview)).append(" Access Denied");
            }
        }
    }
}
