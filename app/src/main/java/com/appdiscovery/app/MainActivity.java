package com.appdiscovery.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appdiscovery.app.services.DiscoverApp;
import com.appdiscovery.app.services.LoadApp;
import com.appdiscovery.app.services.LocationWatcher;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private WebApp[] webapps;

    MainActivity() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.apps_list);
        mRecyclerView.setHasFixedSize(true);
        mLocationWatcher.start();
    }

    private LocationWatcher mLocationWatcher = new LocationWatcher(this, location -> {
        DiscoverApp.byLocation(location, webapps -> {
            MainActivity.this.webapps = webapps;
            mAdapter = new AppListAdapter(webapps, MainActivity.this.onListItemClick);
            mRecyclerView.setAdapter(mAdapter);
            mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
        });
    });

    private View.OnClickListener onListItemClick = (view -> {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        WebApp webapp = webapps[itemPosition];
        LoadApp.show(this, webapp.id);
    });
}
