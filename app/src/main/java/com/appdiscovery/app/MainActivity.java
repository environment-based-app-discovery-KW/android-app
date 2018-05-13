package com.appdiscovery.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appdiscovery.app.services.DigitalSignature;
import com.appdiscovery.app.services.DiscoverApp;
import com.appdiscovery.app.services.LocationWatcher;
import com.appdiscovery.app.services.WidgetAlarmService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mEditUserProfileBtn;
    private WebApp[] webapps = new WebApp[0];
    public static String activeAppName = "";

    MainActivity() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DigitalSignature.init(this);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.apps_list);
        mRecyclerView.setHasFixedSize(true);
        mLocationWatcher.start();
        WebApp.setContext(this);
        mEditUserProfileBtn = findViewById(R.id.edit_user_profile_btn);
        mEditUserProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditUserProfileActivity.class);
            startActivity(intent);
        });
        this.discoverAppByLan();
        WidgetAlarmService.start(this);
    }

    private LocationWatcher mLocationWatcher = new LocationWatcher(this, this::discoverAppByLocation);

    private void setListView(WebApp[] webapps) {
        MainActivity.this.webapps = webapps;
        mAdapter = new AppListAdapter(webapps, MainActivity.this.onListItemClick);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void discoverAppByLocation(Location location) {
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
            for (WebApp app : MainActivity.this.webapps) {
                if (app.distance_in_m < 0) {
                    mergedWebApps.add(app);
                }
            }
            mergedWebApps.addAll(Arrays.asList(webapps));
            setListView(mergedWebApps.toArray(new WebApp[mergedWebApps.size()]));
        });
    }

    private void discoverAppByLan() {
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
            for (WebApp app : MainActivity.this.webapps) {
                if (app.distance_in_m >= 0) {
                    mergedWebApps.add(app);
                }
            }
            setListView(mergedWebApps.toArray(new WebApp[mergedWebApps.size()]));
        });
    }

    private View.OnClickListener onListItemClick = (view -> {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        WebApp webapp = webapps[itemPosition];
        try {
            activeAppName = webapp.name;
            webapp.launch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 初次通过地理位置权限请求
        if (requestCode == LocationWatcher.REQUEST_PERMISSION_LOCATION_STATE) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            discoverAppByLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }
}
