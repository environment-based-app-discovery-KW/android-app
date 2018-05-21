package com.appdiscovery.app.services;

import android.location.Location;

import com.appdiscovery.app.Config;
import com.appdiscovery.app.WebApp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoverApp {
    public static void byLocation(Location location, Consumer<WebApp[]> callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.getRepoServerAddr() + "/app/discover?lat=" + location.getLatitude() + "&lng=" + location.getLongitude())
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            Gson gson = new Gson();
            callback.accept(gson.fromJson(jsonData, WebApp[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void byLan(Consumer<WebApp[]> callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.getInstance().lanRepoServerAddr + "/app/lan-discover")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            Gson gson = new Gson();
            callback.accept(gson.fromJson(jsonData, WebApp[].class));
        } catch (IOException ignored) {
        }
    }
}
