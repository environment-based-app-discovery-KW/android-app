package com.appdiscovery.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appdiscovery.app.services.AppBuilder;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class WebApp {
    private static Context context;
    public Integer id;
    public Double distance_in_m;
    public String name;
    public String description;
    public String developer_id;
    public String created_at;
    public String updated_at;
    public WebAppDependency deps[];
    public WebAppVersion latest_version;

    public static void setContext(Context context) {
        WebApp.context = context;
    }

    public boolean isAppLoaded() {
        String filePath = Utils.getFilePath(context, this.latest_version.code_bundle_hash + ".js");
        return new File(filePath).exists();
    }

    public void preload() {
        if (this.isAppLoaded()) {
            Log.d("APP_PRELOAD", "APPID: " + String.valueOf(this.id) + ", already loaded");
            return;
        }
        Log.d("APP_PRELOAD", "APPID: " + String.valueOf(this.id) + ", preloading..");
        try {
            this.download();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("APP_PRELOAD", "APPID: " + String.valueOf(this.id) + ", preload finish");
    }

    public void download() throws IOException {
        this.download(false);
    }

    public void download(boolean runOnFinish) throws IOException {
        ArrayList<String> depsCodeFiles = new ArrayList<>();
        for (WebAppDependency dep : this.deps) {
            depsCodeFiles.add(Utils.downloadFile(context, dep.code_bundle_hash, ".js"));
        }
        File htmlFile = AppBuilder.build(context, this.deps, this.latest_version.code_bundle_hash);
        if (runOnFinish) {
            Intent myIntent = new Intent(context, WebViewActivity.class);
            myIntent.putExtra("fileName", htmlFile.getAbsolutePath());
            context.startActivity(myIntent);
        }
    }

    public void downloadAndRun() throws IOException {
        this.download(true);
    }
}
