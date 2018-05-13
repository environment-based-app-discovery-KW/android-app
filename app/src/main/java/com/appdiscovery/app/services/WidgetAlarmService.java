package com.appdiscovery.app.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.appdiscovery.app.AppsWidgetProvider;

public class WidgetAlarmService {
    public static boolean isStarted = false;

    public static void start(Context context) {
        if (isStarted) {
            return;
        }
        isStarted = true;

        final Intent intent = new Intent(context, AppsWidgetProvider.class);
        intent.setAction("use_custom_class");
        final PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = 60000;
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pending);
    }
}
