package com.example.urvish.websocketdemo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    private Button mBtnStart;
    private TextView mtextoutput;
    private OkHttpClient mOkHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStart=findViewById(R.id.start);
        mtextoutput=findViewById(R.id.output);
        mOkHttpClient=new OkHttpClient();
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init() {
        Request request=new Request.Builder().url("wss://echo.websocket.org").addHeader("Authorization","Bearer"+1010).build();
        EchoWebSocketListener echoWebSocketListener=new EchoWebSocketListener();
        WebSocket webSocket=mOkHttpClient.newWebSocket(request,echoWebSocketListener);
        mOkHttpClient.dispatcher().executorService().shutdown();
    }
    public void output(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mtextoutput.setText(mtextoutput.getText().toString()+"\n\n"+text);
            }
        });
    }

    public class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;


        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hii, this is urvish!");
            webSocket.send("How you doin'?");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.close(NORMAL_CLOSURE_STATUS,"infinitIniesta!");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving"+text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving"+bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            output("Error : " + t.getLocalizedMessage());
        }

    }

}
