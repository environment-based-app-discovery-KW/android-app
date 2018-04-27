package com.appdiscovery.app.services;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.function.Consumer;

public class LocationWatcher {
    public static final int REQUEST_PERMISSION_LOCATION_STATE = 123;
    private final Consumer<Location> onLocationChangeCallback;
    private final Context context;

    public LocationWatcher(Context context, Consumer<Location> onLocationChangeCallback) {
        this.context = context;
        this.onLocationChangeCallback = onLocationChangeCallback;
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions((Activity) this.context,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    public void start() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                onLocationChangeCallback.accept(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this.context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("请求位置权限", "根据用户所在位置发现APP", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_LOCATION_STATE);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_LOCATION_STATE);
            }
            ActivityCompat.requestPermissions((Activity) this.context, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }
}
