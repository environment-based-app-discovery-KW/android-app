package com.appdiscovery.app.services;

import android.content.Context;

import com.appdiscovery.app.Config;
import com.appdiscovery.app.MainActivity;
import com.appdiscovery.app.WebApp;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadApp {
    public static void show(Context context, Integer id) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.getInstance().repoServerAddr + "/app/download?id=" + id)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            Gson gson = new Gson();
            WebApp app = gson.fromJson(jsonData, WebApp.class);
            app.launch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
