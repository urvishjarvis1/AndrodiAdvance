package com.example.urvish.androidnativeapinn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

     private final String LOG_TAG = "NNAPI_DEMO";
    private long modelHandle = 0;

    public native long initModel(AssetManager assetManager, String assetName);

    public native float startCompute(long modelHandle, float input1, float input2);

    public native void destroyModel(long modelHandle);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InitModelTask().execute("model_data.bin");


         Button compute = ( Button) findViewById(R.id.button);
        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelHandle != 0) {
                     EditText edt1 = ( EditText) findViewById(R.id.inputValue1);
                     EditText edt2 = ( EditText) findViewById(R.id.inputValue2);

                    String inputValue1 = edt1.getText().toString();
                    String inputValue2 = edt2.getText().toString();
                    if (!inputValue1.isEmpty() && !inputValue2.isEmpty()) {
                         Toast.makeText(getApplicationContext(), "Computing",

                                 Toast.LENGTH_SHORT).show();
                        new ComputeTask().execute(
                                Float.valueOf(inputValue1),
                                Float.valueOf(inputValue2));
                    }
                } else {

                     Toast.makeText(getApplicationContext(), "Model initializing, please wait",
                             Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (modelHandle != 0) {
            destroyModel(modelHandle);
            modelHandle = 0;
        }
        super.onDestroy();
    }

    private class InitModelTask extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... modelName) {
            if (modelName.length != 1) {
               Log.e(LOG_TAG, "Incorrect number of model files");
                return 0l;
            }
            // Prepare the model in a separate thread.
            return initModel(getAssets(), modelName[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            modelHandle = result;
        }
    }

    private class ComputeTask extends AsyncTask<Float, Void, Float> {
        @Override
        protected Float doInBackground(Float... inputs) {
            if (inputs.length != 2) {
               Log.e(LOG_TAG, "Incorrect number of input values");
                return 0.0f;
            }
            // Reusing the same prepared model with different inputs.
            return startCompute(modelHandle, inputs[0], inputs[1]);
        }

        @Override
        protected void onPostExecute(Float result) {
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(String.valueOf(result));
        }
    }
    static {
        System.loadLibrary("native-lib");
    }
}
