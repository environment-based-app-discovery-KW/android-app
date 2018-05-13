package com.appdiscovery.app.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.appdiscovery.app.AppsWidgetProvider;
import com.appdiscovery.app.MainActivity;
import com.appdiscovery.app.R;
import com.appdiscovery.app.WebApp;

import java.io.IOException;

class AppsWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;
    private int appWidgetId;
    private WebApp[] webapps;

    public AppsWidgetViewsFactory(Context context) {
        this.context = context;
        this.webapps = AppsWidgetProvider.webapps;
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return (this.webapps.length);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(),
                R.layout.app_widget_list_item);
        row.setTextViewText(android.R.id.text1, this.webapps[position].display_name);
        Intent i = new Intent();
        i.putExtra("position", position);
        row.setOnClickFillInIntent(android.R.id.text1, i);
        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}
