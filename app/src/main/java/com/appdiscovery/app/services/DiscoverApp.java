package com.appdiscovery.app.services;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class DiscoverApp {

    private static final int REQUEST_PERMISSION_LOCATION_STATE = 123;

    private static void showExplanation(Context context,
                                        String title,
                                        String message,
                                        final String permission,
                                        final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(context, permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private static void requestPermission(Context context, String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{permissionName}, permissionRequestCode);
    }

    public static void byGpsLocation(Context context) {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("locationChanged", location.toString());
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

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation(context, "Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_LOCATION_STATE);
            } else {
                requestPermission(context, Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_LOCATION_STATE);
            }
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        Log.d("locationChanged", locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString());
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }
}
