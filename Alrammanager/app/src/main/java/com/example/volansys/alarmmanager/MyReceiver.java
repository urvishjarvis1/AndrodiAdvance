package com.example.volansys.alarmmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private String channel = "Wake Up";
    private String channelId = "wakupAlarm";
    private String channelDesc = "WakeUP";
    final int NOTIFICATION_ID = 1;
    private static String TAG="DOZE DEMO";
    private NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification("WakeUP",context);
        Log.d("str","onrec");
    }
    private void createNotification(String msg,Context context) {

        NotificationCompat.Builder builder;
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mchannel = notificationManager.getNotificationChannel(channelId);
            if (mchannel == null) {
                mchannel = new NotificationChannel(channelId, channel, NotificationManager.IMPORTANCE_HIGH);
                long[] longs = new long[]{100l, 200l, 300l, 400l, 500l, 400l, 300l, 200l, 400l};
                mchannel.setDescription(channelDesc);
                mchannel.enableVibration(true);
                mchannel.setVibrationPattern(longs);
                notificationManager.createNotificationChannel(mchannel);
            }
            builder = new NotificationCompat.Builder(context, channelId);



            builder.setContentTitle(msg)  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(context.getString(R.string.app_name))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker(msg)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(context);



            builder.setContentTitle(msg)                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(context.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker(msg)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        Log.d(TAG, "createNotification: Notification fired!!");
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
