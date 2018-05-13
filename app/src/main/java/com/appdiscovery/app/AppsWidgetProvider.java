package com.appdiscovery.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.appdiscovery.app.services.AppsWidgetService;
import com.appdiscovery.app.services.DiscoverApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//TODO: refactor
public class AppsWidgetProvider extends BroadcastReceiver {
    private ListView mListView;
    public static WebApp[] webapps = new WebApp[0];

    private View.OnClickListener onListItemClick = (view -> {
        int itemPosition = mListView.getSelectedItemPosition();
        WebApp webapp = webapps[itemPosition];
        try {
            MainActivity.activeAppName = webapp.name;
            webapp.launch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    private void setListView(WebApp[] webapps) {
        this.webapps = webapps;
    }

    private void discoverAppByLocation(Location location, Context context) {
        DiscoverApp.byLocation(location, webapps -> {
            for (WebApp webapp : webapps) {
                new Thread(() -> {
                    try {
                        webapp.download();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            ArrayList<WebApp> mergedWebApps = new ArrayList<>();
            for (WebApp app : this.webapps) {
                if (app.distance_in_m < 0) {
                    mergedWebApps.add(app);
                }
            }
            mergedWebApps.addAll(Arrays.asList(webapps));
            setListView(mergedWebApps.toArray(new WebApp[mergedWebApps.size()]));
        });
    }

    private void discoverAppByLan(Context context) {
        DiscoverApp.byLan(webapps -> {
            for (WebApp webapp : webapps) {
                new Thread(() -> {
                    try {
                        webapp.download();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            ArrayList<WebApp> mergedWebApps = new ArrayList<>(Arrays.asList(webapps));
            for (WebApp app : this.webapps) {
                if (app.distance_in_m >= 0) {
                    mergedWebApps.add(app);
                }
            }
            setListView(mergedWebApps.toArray(new WebApp[mergedWebApps.size()]));
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("intent.action", intent.getAction());
        if ("SHOW_APP".equals(intent.getAction())) {
            int position = intent.getIntExtra("position", -1);
            WebApp webapp = webapps[position];
            try {
                MainActivity.activeAppName = webapp.name;
                webapp.launch();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("UPDATE_WIDGET".equals(intent.getAction())) {
            WebApp.setContext(context);
            this.discoverAppByLan(context);
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);

            Intent svcIntent = new Intent(context, AppsWidgetService.class);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            updateViews.setRemoteAdapter(R.id.widget_apps_list, svcIntent);
            ComponentName thisWidget = new ComponentName(context, AppsWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), AppsWidgetProvider.class.getName());
            int[] appWidgetIds = manager.getAppWidgetIds(thisAppWidget);
            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_apps_list);

            Intent showAppIntent = new Intent(context, AppsWidgetProvider.class);
            showAppIntent.setAction("SHOW_APP");
            PendingIntent clickPI = PendingIntent.getBroadcast(context, 0, showAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setPendingIntentTemplate(R.id.widget_apps_list, clickPI);

            manager.updateAppWidget(thisWidget, updateViews);
        }
    }
}
