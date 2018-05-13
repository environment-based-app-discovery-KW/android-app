package com.appdiscovery.app.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class AppsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new AppsWidgetViewsFactory(this.getApplicationContext()));
    }
}
