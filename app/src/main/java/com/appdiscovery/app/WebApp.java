package com.appdiscovery.app;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.appdiscovery.app.services.AppBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebApp {
    public static HashMap<String, Boolean> mapAppIdToRunOnFinish = new HashMap<>();
    private static Context context;
    public Integer id;
    public Double distance_in_m;
    public String name;
    public String description;
    public String developer_id;
    public String created_at;
    public String updated_at;
    public String launch_params_json;
    public String display_name;
    public WebAppDependency deps[];
    public WebAppVersion latest_version;

    public static void setContext(Context context) {
        WebApp.context = context;
    }

    public boolean isAppDownloaded() {
        String filePath = Utils.getFilePath(context, this.latest_version.code_bundle_hash + ".js");
        return new File(filePath).exists();
    }

    public void download() throws IOException {
        if (!this.isAppDownloaded()) {
            ArrayList<String> depsCodeFiles = new ArrayList<>();
            for (WebAppDependency dep : this.deps) {
                depsCodeFiles.add(Utils.downloadFile(context, dep.code_bundle_hash, ".js"));
            }
            Utils.downloadFile(context, this.latest_version.code_bundle_hash, ".js");
        }

        if (mapAppIdToRunOnFinish.getOrDefault(this.id.toString(), false)) {
            this.launch();
        }
    }

    public void launch() throws IOException {
        if (this.isAppDownloaded()) {
            File file = AppBuilder.build(context, this.deps, this.latest_version.code_bundle_hash, this.launch_params_json);
            Intent myIntent = new Intent(context, WebViewActivity.class);
            myIntent.putExtra("fileName", file.getAbsolutePath());
            context.startActivity(myIntent);
        } else {
            Toast.makeText(context, "APP正在传输中，将很快为您打开", Toast.LENGTH_LONG).show();
            mapAppIdToRunOnFinish.put(this.id.toString(), true);
        }
    }
}
