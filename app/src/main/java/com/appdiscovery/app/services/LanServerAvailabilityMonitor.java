package com.appdiscovery.app.services;

import android.os.Handler;
import android.util.Log;

import com.appdiscovery.app.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//TODO: listen for network change event and trigger rediscovery

public class LanServerAvailabilityMonitor {
    public static boolean lanAvailable = false;
    public static final String TAG = "LanServerAvailabilityMonitor";
    private static boolean isStarted = false;

    public static void start() {
        if (isStarted) {
            return;
        }
        isStarted = true;
        checkAvailability();
        Handler handler = new Handler();
        int interval = 5000;
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                checkAvailability();
                handler.postDelayed(this, interval);
            }
        }, interval);
    }

    public static void checkAvailability() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.getInstance().lanRepoServerAddr)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObj = new JSONObject(response.body().string());
            lanAvailable = jsonObj.getBoolean("online");
        } catch (IOException | JSONException ignored) {
            lanAvailable = false;
        }
        Log.d(TAG, "Lan server availability: " + String.valueOf(lanAvailable));
    }
}
