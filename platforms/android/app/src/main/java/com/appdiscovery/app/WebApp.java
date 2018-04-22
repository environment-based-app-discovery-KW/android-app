package com.appdiscovery.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class WebApp {
    private Context context;
    public Integer id;
    public String name;
    public String description;
    public String developer_id;
    public String created_at;
    public String updated_at;
    public WebAppDependency deps[];
    public WebAppVersion latestVersion;

    public void setContext(Context context) {
        this.context = context;
    }

    public void downloadAndRun() {
        //TODO: cache
        String appCodeFile = Utils.getInstance().downloadFile(this.context, this.latestVersion.code_bundle_hash, ".js");
        ArrayList<String> depsCodeFiles = new ArrayList<>();
        for (WebAppDependency dep : this.deps) {
            depsCodeFiles.add(Utils.getInstance().downloadFile(this.context, dep.code_bundle_hash, ".js"));
        }

    }
}
