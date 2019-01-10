package com.example.urvish.ndkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int called=0;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        TextView simpleCall=findViewById(R.id.sample_call);
        float ans=computeGforce();
        simpleCall.setTextSize(20);
        simpleCall.setText("ans is:   "+ans);
        callback(called);
    }
    public void callBack(int c){
        called++;
        c=called;
        Log.d("Called",":"+called);
        if(called<5){
            callback(c);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native float computeGforce();
    public native void callback(int c);
}
