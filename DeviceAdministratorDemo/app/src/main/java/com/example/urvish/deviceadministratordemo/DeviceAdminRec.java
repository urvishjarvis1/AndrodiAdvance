package com.example.urvish.deviceadministratordemo;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Toast;

public class DeviceAdminRec extends DeviceAdminReceiver {
    private static String TAG=DeviceAdminRec.class.getName();
    @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle user) {
        super.onPasswordChanged(context, intent, user);
        Toast.makeText(context, "Password Changed!", Toast.LENGTH_SHORT).show();
        DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName=new ComponentName(context,DeviceAdminRec.class);
        dpm.setPasswordExpirationTimeout(componentName,10000L);
    }

    @Override
    public void onPasswordExpiring(Context context, Intent intent, UserHandle user) {
        super.onPasswordExpiring(context, intent, user);
        Log.d(TAG, "onPasswordExpiring: Password Expired");
        Toast.makeText(context, "Password expired", Toast.LENGTH_SHORT).show();
    }
}
