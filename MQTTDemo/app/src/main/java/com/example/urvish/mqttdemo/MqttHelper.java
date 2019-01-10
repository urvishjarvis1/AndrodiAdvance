package com.example.urvish.mqttdemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttHelper {
    public MqttAndroidClient mqttAndroidClient;
    public static final String URL = "tcp://m14.cloudmqtt.com:15084";
    public static final String CLIENTID = "AndroidDemo";
    public static final String SUBTOPIC = "sensor/+";
    public static final String TAG = "Mqtt";
    private boolean isSub = false, isConnect = false;
    private Context context;


    public MqttHelper(Context context) {
        this.context = context;
        mqttAndroidClient = new MqttAndroidClient(context, URL, CLIENTID);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d(TAG, "connectComplete: " + serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "connectionLost: " + cause.getLocalizedMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "messageArrived: Topic:" + topic + " Message" + message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void setCallBack(MqttCallbackExtended callBack) {
        mqttAndroidClient.setCallback(callBack);
    }

    public void connect() {
        String password = "XDzwC9mbBjjh";
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName("kjkxufte");
        mqttConnectOptions.setPassword(password.toCharArray());
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: success");
                    isConnect = true;
                    Toast.makeText(context, "Connection successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "onFailure: failure" + exception.getLocalizedMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "connect: exception" + e.getMessage());
        }

    }

    public void subscribe() {
       if(isConnect){
        try {
            mqttAndroidClient.subscribe(SUBTOPIC, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess:Subscried ");
                    isSub = true;
                    Toast.makeText(context, "Subscribed to " + SUBTOPIC + "Topic(s).", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "onFailure: Failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "subscribe: " + e.getMessage());
        }
       }
    }

    public void unsubscribe() {
        if (isSub) {
            try {
                mqttAndroidClient.unsubscribe(SUBTOPIC, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG, "onSuccess: Unsubscribe");
                        Toast.makeText(context, "Unsubscribe", Toast.LENGTH_SHORT).show();
                        isSub=false;
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "onFailure: Failed to unsuscribe" + exception.getMessage());

                        Toast.makeText(context, "Failed to unsubscribe", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public void disconnect() {
        if (isConnect) {
            try {
                mqttAndroidClient.disconnect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG, "onSuccess: Success");
                        Toast.makeText(context, "Disconnected from cloud", Toast.LENGTH_SHORT).show();
                        isConnect=false;
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "onFailure: Failed" + exception.getLocalizedMessage());
                        Toast.makeText(context, "failed to disconnect", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }
    public void publish(String msg){
        if(isConnect){
            try{

                mqttAndroidClient.publish("sensor/", msg.getBytes(), 0, false, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG, "onSuccess: Published");
                        Toast.makeText(context, "Error in publishing", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "onFailure: Failure"+exception.getLocalizedMessage() );
                        Toast.makeText(context, "Published", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MqttPersistenceException e) {
                e.printStackTrace();
                Log.e(TAG, "publish: error"+e.getLocalizedMessage() );
            } catch (MqttException e) {
                e.printStackTrace();
                Log.e(TAG, "publish: error"+e.getLocalizedMessage() );
            }
        }

    }


}
