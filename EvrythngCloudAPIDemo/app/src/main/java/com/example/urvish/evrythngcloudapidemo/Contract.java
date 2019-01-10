package com.example.urvish.evrythngcloudapidemo;

public class Contract {
    private static final String API_KEY="VPnfJbB3gi7HAfi3LNr33WxqMiHfHxjfHgYNM7b8w0g9Mt0ELsRWuYcmBCFGhzPx9Gw7xIbZ1veAizXP";
    private static Contract sContract=new Contract();
    private Contract(){

    }
    public static String getApiKey(){
        return API_KEY;
    }
    public static Contract getsContract() {
        return sContract;
    }
}
