package com.example.urvish.homewidgetdemo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class CounterWidger extends AppWidgetProvider {
    private static int mCounter=0;
    private static final String ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {



        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widger);
        Intent intent=new Intent(context,CounterWidger.class);
        intent.setAction(ACTION_SIMPLEAPPWIDGET);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(ACTION_SIMPLEAPPWIDGET.equals(intent.getAction())){
            mCounter++;
            RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.counter_widger);
            views.setTextViewText(R.id.appwidget_text,""+mCounter);
            ComponentName appWidget=new ComponentName(context,CounterWidger.class);
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(appWidget,views);
        }
    }
}

