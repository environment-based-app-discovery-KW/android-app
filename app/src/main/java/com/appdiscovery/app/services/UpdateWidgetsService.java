package com.appdiscovery.app.services;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.appdiscovery.app.AppsWidgetProvider;
import com.appdiscovery.app.R;

import java.util.Random;

public class UpdateWidgetsService extends Service {

    UpdateWidgetsService() {
        Log.d("UpdateWidgetsService", "created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RemoteViews updateViews = new RemoteViews(
                this.getPackageName(), R.layout.app_widget_layout);
        Log.d("UpdateWidgetsService", "updating..");
        updateViews.setTextViewText(R.id.update, String.valueOf(new Random().nextInt(100)));

        ComponentName thisWidget = new ComponentName(this, AppsWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, updateViews);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
