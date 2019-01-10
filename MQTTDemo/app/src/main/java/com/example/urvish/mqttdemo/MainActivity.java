package com.example.urvish.mqttdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    private TextView mTxtViewData;
    private MqttHelper mqttHelper;
    private Button mBtnConnect,mBtnDisconnect,mBtnSubscribe,mBtnUnsubscribe,mBtnPublish;
    private EditText medittextData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtViewData=findViewById(R.id.datafromcloud);
        mBtnConnect=findViewById(R.id.btnconnet);
        mBtnSubscribe=findViewById(R.id.subscribe);
        mBtnUnsubscribe=findViewById(R.id.unsubscribe);
        mBtnDisconnect=findViewById(R.id.btnDisconnect);
        mBtnPublish=findViewById(R.id.publish);
        medittextData=findViewById(R.id.edittextdata);
        mqttHelper=new MqttHelper(this);
        mqttHelper.setCallBack(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                mTxtViewData.setText(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.connect();
            }
        });
        mBtnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.subscribe();
            }
        });
        mBtnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.unsubscribe();
                }
        });
        mBtnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.disconnect();
            }
        });
        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!medittextData.getText().toString().isEmpty())
                     mqttHelper.publish(medittextData.getText().toString());
                else
                    medittextData.setError("enter data");
            }
        });
    }
}
