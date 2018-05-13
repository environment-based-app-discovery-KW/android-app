package com.appdiscovery.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

public class AppsWidgetProvider extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("UpdateWidgetsService", "onReceive");
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);
        Log.d("UpdateWidgetsService", "updating..");
        updateViews.setTextViewText(R.id.update, String.valueOf(new Random().nextInt(100)));
        Intent pintent = new Intent(context, AppsWidgetProvider.class);
        pintent.setAction("use_custom_class");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, pintent, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.update, pendingIntent);
        ComponentName thisWidget = new ComponentName(context, AppsWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, updateViews);
    }
}
