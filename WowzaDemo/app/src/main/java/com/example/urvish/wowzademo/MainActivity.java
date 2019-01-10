package com.example.urvish.wowzademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.broadcast.WZGLBroadcaster;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZError;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

public class MainActivity extends AppCompatActivity implements WZStatusCallback {
    private WowzaGoCoder wowzaGoCoder;
    private WZCameraView wzCameraView;
    private WZAudioDevice wzAudioDevice;
    private WZBroadcastConfig wzBroadcastConfig;
    private WZBroadcast broadcaster;
    private final static int PERMISSION_CODE=1;
    private final static String TAG="MainActivity.this";
    private boolean mPermissionGranted=true;
    private String [] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View rootGroup=getWindow().getDecorView().findViewById(android.R.id.content);
        if(rootGroup!=null){
            rootGroup.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wowzaGoCoder=WowzaGoCoder.init(this, "GOSK-4345-0103-F25D-D684-2DA5");
        if(wowzaGoCoder==null){
            WZError wzError=WowzaGoCoder.getLastError();
            Log.e(TAG, "onCreate: "+wzError.getErrorDescription() );
        }
        wzCameraView=findViewById(R.id.camera_preview);
        wzAudioDevice=new WZAudioDevice();
        wzBroadcastConfig=new WZBroadcastConfig(WZBroadcastConfig.FRAME_SIZE_1920x1080);
        broadcaster=new WZBroadcast();
        wzBroadcastConfig.setHostAddress("5549ab.entrypoint.cloud.wowza.com");
        wzBroadcastConfig.setPortNumber(1935);
        wzBroadcastConfig.setApplicationName("app-7238");
        wzBroadcastConfig.setStreamName("066c95c1");
        wzBroadcastConfig.setVideoBroadcaster(wzCameraView);
        wzBroadcastConfig.setAudioBroadcaster(wzAudioDevice);
        ((Button)findViewById(R.id.broadcast_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPermissionGranted)return;
                WZStreamingError wzStreamingError=wzBroadcastConfig.validateForBroadcast();
                if(wzStreamingError!=null){
                    Log.e(TAG, "onClick: "+wzStreamingError.getErrorDescription());
                }else if(broadcaster.getStatus().isRunning()){
                    broadcaster.endBroadcast(MainActivity.this);
                }else{
                    broadcaster.startBroadcast(wzBroadcastConfig,MainActivity.this);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            mPermissionGranted=hasPermission(this,permissions);
            if(!mPermissionGranted){
                ActivityCompat.requestPermissions(this,permissions,PERMISSION_CODE);
            }
        }else{
            mPermissionGranted=true;
        }
        if(mPermissionGranted&&wzCameraView!=null){
            if(wzCameraView.isPreviewPaused()){
                wzCameraView.onResume();
            }
            else {
                wzCameraView.startPreview();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(wzCameraView!=null){
            wzCameraView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcaster!=null){
            broadcaster.endBroadcast(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionGranted=true;
        switch (requestCode){
            case PERMISSION_CODE:{
                for(int result:grantResults){
                    if(result!=PackageManager.PERMISSION_GRANTED){
                        mPermissionGranted=false;
                    }
                }
            }

        }

    }

    private boolean hasPermission(MainActivity mainActivity, String[] permissions) {
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(mainActivity,permission)!= PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onWZStatus(WZStatus wzStatus) {
        final StringBuffer statusmsg=new StringBuffer("BroadcastStatus:");
        switch (wzStatus.getState()){
            case WZState.STARTING:
                statusmsg.append("initialization");
                break;
            case WZState.READY:
                statusmsg.append("Ready");
                break;
                case WZState.RUNNING:
                statusmsg.append("Streaming is active");
                break;

            case WZState.STOPPING:
                statusmsg.append("Broadcast shutting down");
                break;

            case WZState.IDLE:
                statusmsg.append("The broadcast is stopped");
                break;

            default:
                return;
        }
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, ""+statusmsg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "run: "+statusmsg);
            }
        });
    }

    @Override
    public void onWZError(final WZStatus wzStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        "Streaming error: " + wzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "run: "+wzStatus.getLastError().getErrorDescription() );
            }
        });
    }
}
