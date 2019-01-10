package com.example.urvish.androidstreamingdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.media.MediaRecorder;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.bytedeco.javacpp.avutil;

import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameFilter;
import org.bytedeco.javacv.FrameRecorder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName() ;
    private static final String TAG =LOG_TAG ;
    private String ffmpegLink= "/mnt/sdcard/stream.flv";
    long startTime = 0;
    boolean recording=false;
    /**
     * FFmpeg Recorder
     */
    private FFmpegFrameRecorder mRecorder;
    /**
     * Filter for FFmpeg
     */
    private boolean isPreviewOn = false;
    private Boolean addFilter =false;
    private String fileStream="";
    FFmpegFrameFilter fFmpegFrameFilter;

    //Video res
    private int sampleAudioRateInHz=44100;
    private int imageWidth=720;
    private int imageHeight=300;
    private int frameRate=30;
    //Audio Data
    private AudioRecord audioRecord;
    private AudioRecordRunnable audioRecordRunnable;//runnable for getting audio.
    private Thread audioThread;
    volatile boolean runAudioThread = true;

    //Video data
    private Camera cameraDevice;

    private CameraView cameraView;//SurfaceView
    private Frame yuvImage=null;
    /*
    Layout config
     */
    private final int bg_screen_bx = 232;
    private final int bg_screen_by = 128;
    private final int bg_screen_width = 700;
    private final int bg_screen_height = 500;
    private final int bg_width = 1123;
    private final int bg_height = 715;
    private final int live_width = 640;
    private final int live_height = 480;
    private int screenWidth, screenHeight;
    private Button btnRecorderControl,filterControll;
    private boolean isPermissiongranted=false;

    final int RECORE_LENGTH=0;//to disableContinues loop.
    Frame[] images;
    long[] timestemps;
    ShortBuffer[] samples;
    int imageIndex,sampleIndex;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        permissionCheck();
        if(isPermissionGiven())
            initlayout();
        else
            Toast.makeText(this, "You need to give permissions", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (7){
            case 7:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    for(String permission:permissions){
                        Toast.makeText(this, "Granted"+permission, Toast.LENGTH_SHORT).show();
                    }
                    isPermissiongranted=true;

                }else{
                    Toast.makeText(this, "Please give permission", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private boolean isPermissionGiven() {
        return isPermissiongranted;
    }

    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},7);
        }else{
            isPermissiongranted=true;
        }
    }

    private void initlayout() {
        /*get size of screen */
        Display display=((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        screenWidth=display.getWidth();
        screenHeight=display.getHeight();
        RelativeLayout.LayoutParams layoutParams=null;
        LayoutInflater layoutInflater=null;
        layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        RelativeLayout topLayout=new RelativeLayout(this);
        setContentView(topLayout);
        LinearLayout preViewLayout=(LinearLayout)layoutInflater.inflate(R.layout.activity_main,null);
        layoutParams = new RelativeLayout.LayoutParams(screenWidth,screenHeight);
        topLayout.addView(preViewLayout,layoutParams);
        //Button for control
        btnRecorderControl = (Button) findViewById(R.id.recorder_control);
        btnRecorderControl.setText("Start");
        btnRecorderControl.setOnClickListener(this);
        filterControll=findViewById(R.id.filter);
        filterControll.setOnClickListener(this);
        /*Add the camera view*/
        int display_width_d = (int) (1.0 * bg_screen_width * screenWidth / bg_width);
        int display_height_d = (int) (1.0 * bg_screen_height * screenHeight / bg_height);
        int prev_rw, prev_rh;
        if (1.0 * display_width_d / display_height_d > 1.0 * live_width / live_height) {
            prev_rh = display_height_d;
            prev_rw = (int) (1.0 * display_height_d * live_width / live_height);
        } else {
            prev_rw = display_width_d;
            prev_rh = (int) (1.0 * display_width_d * live_height / live_width);
        }
        layoutParams = new RelativeLayout.LayoutParams(prev_rw, prev_rh);
        layoutParams.topMargin = (int) (1.0 * bg_screen_by * screenHeight / bg_height);
        layoutParams.leftMargin = (int) (1.0 * bg_screen_bx * screenWidth / bg_width);


        cameraDevice = Camera.open();
        Log.i(LOG_TAG, "cameara open");
        cameraView = new CameraView(this, cameraDevice);
        topLayout.addView(cameraView, layoutParams);
        Log.i(LOG_TAG, "cameara preview start: OK");
    }
    private void initRecoreder(){
        Log.d(TAG, "initRecoreder: ");
        if(RECORE_LENGTH>0) {
            imageIndex = 0;
            images = new Frame[RECORE_LENGTH * frameRate];
            timestemps = new long[images.length];
            for (int i = 0; i < images.length; i++) {
                images[i] = new Frame(imageWidth, imageHeight, Frame.DEPTH_UBYTE, 2);
                timestemps[i] = -1;
            }
        }else if (yuvImage==null){
            yuvImage = new Frame(imageWidth, imageHeight, Frame.DEPTH_UBYTE, 2);
            Log.i(LOG_TAG, "create yuvImage");
        }
        Log.d(TAG, "initRecoreder: "+ffmpegLink);
        mRecorder=new FFmpegFrameRecorder(ffmpegLink,imageWidth,imageHeight,1);
        mRecorder.setFormat("flv");
        mRecorder.setSampleRate(sampleAudioRateInHz);
        mRecorder.setFrameRate(frameRate);

        fileStream="transpose=2";
        fFmpegFrameFilter=new FFmpegFrameFilter(fileStream,imageWidth,imageHeight);
        fFmpegFrameFilter.setPixelFormat(avutil.AV_PIX_FMT_NV21);
        Log.i(LOG_TAG, "recorder initialize success");

        audioRecordRunnable = new AudioRecordRunnable();
        audioThread = new Thread(audioRecordRunnable);
        runAudioThread = true;

    }
    public void startRecording(){
        initRecoreder();
        try{
             mRecorder.start();
             startTime=System.currentTimeMillis();
             recording=true;
             audioThread.start();
             if(addFilter){
                 fFmpegFrameFilter.start();
             }
        } catch (FrameRecorder.Exception | FrameFilter.Exception e) {
            e.printStackTrace();
        }
    }
    private void stopRecording() {
        runAudioThread = false;
        try {
            audioThread.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;

        }
        audioRecordRunnable = null;
        audioThread = null;
        if (mRecorder != null && recording) {
            if (RECORE_LENGTH > 0) {
                Log.d(TAG, "stopRecording: Writing frames");
                try {
                    int firstIndex = imageIndex % samples.length;
                    int lastIndex = (imageIndex - 1) % images.length;
                    if (imageIndex <= images.length) {
                        firstIndex = 0;
                        lastIndex = imageIndex - 1;
                    }
                    if ((startTime = timestemps[lastIndex] - RECORE_LENGTH * 1000000L) < 0) {
                        startTime = 0;
                    }
                    if (lastIndex < firstIndex) {
                        lastIndex += images.length;
                    }
                    for (int i = firstIndex; i <= lastIndex; i++) {
                        long t = timestemps[i % timestemps.length] - startTime;
                        if (t >= 0) {
                            if (t > mRecorder.getTimestamp()) {
                                mRecorder.setTimestamp(t);
                            }
                            mRecorder.record(images[i % images.length]);
                        }
                    }
                    firstIndex = sampleIndex % samples.length;
                    lastIndex = (sampleIndex - 1) % samples.length;
                    if (sampleIndex <= samples.length) {
                        firstIndex = 0;
                        lastIndex = sampleIndex - 1;
                    }
                    if (lastIndex < firstIndex) {
                        lastIndex += samples.length;
                    }
                    for (int i = firstIndex; i <= lastIndex; i++) {
                        mRecorder.recordSamples(samples[i % samples.length]);
                    }

                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }

            }

            recording = false;
            Log.d(TAG, "stopRecording: Finishing recording");
            try {
                mRecorder.stop();
                mRecorder.release();
                fFmpegFrameFilter.stop();
                fFmpegFrameFilter.release();
            } catch (FrameRecorder.Exception | FrameFilter.Exception e) {
                e.printStackTrace();
            }

        mRecorder = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (recording) {
                stopRecording();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        recording = false;

        if (cameraView != null) {
            cameraView.stopPreview();
        }

        if(cameraDevice != null) {
            cameraDevice.stopPreview();
            cameraDevice.release();
            cameraDevice = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recorder_control:
                if(!recording){
                    startRecording();
                    Log.d(TAG, "onClick: button");
                    btnRecorderControl.setText("stop");
                }else {
                    stopRecording();
                    btnRecorderControl.setText("Start");
                }
            break;
            case R.id.filter:

                if(!recording&&!addFilter){
                    addFilter=true;
                    if(addFilter)
                        filterControll.setText("Remove Filter");
                }else if(!recording&&addFilter){
                    addFilter=false;
                    if(!addFilter)
                        filterControll.setText(R.string.apply_filter);
                }
                else{
                    addFilter=false;
                    //Toast.makeText(this, "Filter connot be applied to ongoing recording please stop the recording and apply filter!", Toast.LENGTH_SHORT).show();
                    if(!addFilter)
                        filterControll.setText(R.string.apply_filter);
                }



                break;
        }

    }
    //audio thread
    class AudioRecordRunnable implements Runnable{

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            int bufferSize;
            ShortBuffer audioData;
            int bufferReadResult;
            bufferSize=AudioRecord.
                    getMinBufferSize(sampleAudioRateInHz,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new
                    AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleAudioRateInHz,AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,bufferSize);
            if(RECORE_LENGTH>0){
                sampleIndex = 0;
                samples=new ShortBuffer[RECORE_LENGTH*sampleAudioRateInHz*2/bufferSize+1];
                for(int i=0;i< samples.length;i++){
                    samples[i]=ShortBuffer.allocate(bufferSize);

                }
            }else{
                audioData=ShortBuffer.allocate(bufferSize);
            }
            Log.d(TAG, "run: startrecording");
            audioRecord.startRecording();
            /*ffmpeg_audio encoding loop*/
            while (runAudioThread){
                if(RECORE_LENGTH>0){
                    audioData=samples[sampleIndex++%samples.length];
                    audioData.position(0).limit(0);
                }
                bufferReadResult=audioRecord.read(audioData.array(),0,audioData.capacity());
                audioData.limit(bufferReadResult);
                if(bufferReadResult>0){
                    Log.d(TAG, "run:BufferReadResult: "+bufferReadResult);
                    if(recording){
                        if(RECORE_LENGTH<=0)try{
                            mRecorder.recordSamples(audioData);
                        } catch (FrameRecorder.Exception e) {
                            Log.e(TAG, "run: ",e );
                            e.printStackTrace();
                        }
                    }
                }
            }
            Log.d(TAG, "run: AUDIO thread finished");
            if(audioRecord !=null){
                audioRecord.stop();
                audioRecord.release();
                audioRecord=null;
                Log.d(TAG, "run: AudioRecored Relesed");
            }

        }
    }
    public class CameraView extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{
        private SurfaceHolder mHolder;
        private Camera camera;

        public CameraView(Context context,Camera camera){
            super(context);
            Log.d(TAG, "CameraView: ");
            this.camera=camera;
            mHolder=getHolder();
            mHolder.addCallback(CameraView.this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            this.camera.setPreviewCallback(CameraView.this);
        }
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if(audioRecord==null||audioRecord.getRecordingState()!=AudioRecord.RECORDSTATE_RECORDING){
                startTime=System.currentTimeMillis();
                return;
            }
            if(RECORE_LENGTH>0){
                int i=imageIndex++%images.length;
                yuvImage=images[i];
                timestemps[i]=1000*(System.currentTimeMillis()-startTime);
            }
            if(yuvImage!=null&&recording){
                ((ByteBuffer)yuvImage.image[0].position(0)).put(data);
                if(RECORE_LENGTH<=0)try{
                    Log.d(TAG, "onPreviewFrame: Writing Frames");
                    long t=1000*(System.currentTimeMillis()-startTime);
                    if(t>mRecorder.getTimestamp()){
                        mRecorder.setTimestamp(t);
                    }
                    if(addFilter){
                        Log.d(TAG, "onPreviewFrame: "+addFilter);
                        fFmpegFrameFilter.push(yuvImage);
                        Frame frame2;
                        while ((frame2=fFmpegFrameFilter.pull())!=null){
                            mRecorder.record(frame2, fFmpegFrameFilter.getPixelFormat());

                        }
                    }else{
                        mRecorder.record(yuvImage);
                    }
                } catch (FrameFilter.Exception | FrameRecorder.Exception e) {
                    Log.d(TAG, "onPreviewFrame: "+e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try{
                stopPreview();
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                camera.release();
                camera=null;
                Log.e(TAG, "surfaceCreated: ",e );
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            stopPreview();
            Camera.Parameters parameters=camera.getParameters();
            List<Camera.Size> sizes=parameters.getSupportedPictureSizes();
            Collections.sort(sizes, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size o1, Camera.Size o2) {
                    return o1.width*o1.height-o2.width*o2.height;
                }
            });
            for(int i=0; i <sizes.size(); i++){
                if((sizes.get(i).width>=imageWidth && sizes.get(i).height>=imageHeight)||i==sizes.size()-1){
                    imageWidth=sizes.get(i).width;
                    imageHeight=sizes.get(i).height;
                    Log.d(TAG, "surfaceChanged: Changed to supported res");
                    break;
                }
            }
            parameters.setPreviewSize(imageWidth,imageHeight);
            Log.d(TAG, "surfaceChanged: setting Imagewidth"+imageWidth+"imageHeight"+imageHeight+"framerate:"+frameRate);
            parameters.setPreviewFrameRate(frameRate);
            camera.setParameters(parameters);
            try{
                this.camera.setPreviewDisplay(holder);
                this.camera.setPreviewCallback(CameraView.this);
                startPreview();
            } catch (IOException e) {
                Log.e(TAG, "surfaceChanged: ",e );
                e.printStackTrace();
            }


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
             try{
                 mHolder.addCallback(null);
                 camera.setPreviewCallback(null);
             }catch (RuntimeException e){
                 Log.e(TAG, "surfaceDestroyed: ",e );
             }
        }
        public void startPreview(){
            if(!isPreviewOn&&camera!=null){
                isPreviewOn=true;
                camera.startPreview();
            }
        }
        public void stopPreview(){
            if(isPreviewOn&&camera!=null){
                isPreviewOn=false;
                camera.stopPreview();
            }
        }
    }
}