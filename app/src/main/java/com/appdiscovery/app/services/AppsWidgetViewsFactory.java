package com.appdiscovery.app.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.appdiscovery.app.AppsWidgetProvider;
import com.appdiscovery.app.R;
import com.appdiscovery.app.WebApp;

import java.io.InputStream;

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
    public void onDataSetChanged() {
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

    Bitmap downloadBitmap(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(),
                R.layout.app_widget_list_item);
        row.setImageViewBitmap(R.id.widget_app_image_view, downloadBitmap(this.webapps[position].latest_version.logo_url));
        row.setTextViewText(R.id.widget_app_text, this.webapps[position].display_name);
        Intent i = new Intent();
        i.putExtra("position", position);
        row.setOnClickFillInIntent(R.id.widget_app_text, i);
        row.setOnClickFillInIntent(R.id.widget_app_image_view, i);
        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
