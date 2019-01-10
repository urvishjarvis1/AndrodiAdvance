package com.example.urvish.vxgdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.nio.ByteBuffer;

import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayerConfig;
import veg.mediaplayer.sdk.MediaPlayer.PlayerRecordFlags;

public class MainActivity extends AppCompatActivity implements MediaPlayer.MediaPlayerCallback{
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player=findViewById(R.id.playerView);
        final MediaPlayerConfig config=new MediaPlayerConfig();
        config.setConnectionUrl("http://yt-dash-mse-test.commondatastorage.googleapis.com/media/motion-20120802-85.mp4");

        config.setConnectionNetworkProtocol(2/*RTSP over HTTP tunneling*/);
        config.setConnectionDetectionTime(500);
        config.setConnectionBufferingTime(500/*500ms for buffering*/);
        config.setDecodingType(1/*h/w decoder*/);
        config.setRendererType(1/*pure OpenGl*/);
        config.setSynchroEnable(1/*enable*/);
        config.setSynchroNeedDropVideoFrames(0/*Disable*/);
        config.setEnableColorVideo(0/*grayscale*/);
        config.setAspectRatioMode(1/*full size*/);
        config.setDataReceiveTimeout(30000);
        config.setNumberOfCPUCores(0);
        config.setEnableAudio(1);


        player.Open(config,MainActivity.this);
        Toast.makeText(this, ""+player.getState(), Toast.LENGTH_SHORT).show();

        ((Button)findViewById(R.id.startplayback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.Play();
            }
        });
        ((Button)findViewById(R.id.stopplayback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.Pause();
                Toast.makeText(MainActivity.this, ""+player.getState(), Toast.LENGTH_SHORT).show();

            }
        });
        ((Button)findViewById(R.id.stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.Stop();
                Toast.makeText(MainActivity.this, ""+player.getState(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int Status(int i) {
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        return 0;
    }
    @Override
    protected void onDestroy()
    {
        // Destroy and close player
        if (player != null)
        {
            // Close connection to server
            player.Close();
            // Destroy player
        }
        player.onDestroy();
        super.onDestroy();
    }

}
