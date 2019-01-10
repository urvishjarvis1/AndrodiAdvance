package com.example.volansys.alarmmanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private ToggleButton mtoggleAlarm;
    final int NOTIFICATION_ID = 1;
    private String channel = "Wake Up";
    private String channelId = "wakupAlarm";
    private String channelDesc = "WakeUP";
    private NotificationManager notificationManager;


    private static final String ACTION_NOTIFY="com.example.volansys.alarmmanager.ACTION_NOTIFY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(ACTION_NOTIFY, "onCreate: "+getPackageName());
        mtoggleAlarm = findViewById(R.id.toggleAlarm);
        final AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this,MyReceiver.class);
        final PendingIntent pendingIntent=PendingIntent.getBroadcast(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mtoggleAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //createNotification("Alarm On!!",getApplicationContext());
                    long stime=SystemClock.elapsedRealtime()+10*1000;
                    long dtime=10*1000;
                    Log.d("str","stime:"+stime+"  dtime:"+dtime);
                    alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME,stime,pendingIntent);
                    Toast.makeText(MainActivity.this, "Alarm set", Toast.LENGTH_SHORT).show();
                }
                else {

                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(MainActivity.this, "Alarm off", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        notificationManager.notify(NOTIFICATION_ID, notification);
    }


}

