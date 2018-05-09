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

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mEditUserProfileBtn;
    private WebApp[] webapps;

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
    }

    private LocationWatcher mLocationWatcher = new LocationWatcher(this, this::discoverAppByLocation);

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
            MainActivity.this.webapps = webapps;
            mAdapter = new AppListAdapter(webapps, MainActivity.this.onListItemClick);
            mRecyclerView.setAdapter(mAdapter);
            mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
        });
    }

    private View.OnClickListener onListItemClick = (view -> {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        WebApp webapp = webapps[itemPosition];
        try {
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
