package com.appdiscovery.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\" dir=\"ltr\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title></title>\n"
        );

        // copy cordova assets
        Utils.copyAssetFileOrDir(context, "www/cordova.js", "cordova.js");
        Utils.copyAssetFileOrDir(context, "www/cordova_plugins.js", "cordova_plugins.js");
        Utils.copyAssetFileOrDir(context, "www/plugins", "plugins");
        Utils.copyAssetFileOrDir(context, "www/cordova-js-src", "cordova-js-src");
        Utils.copyAssetFileOrDir(context, "www/sys.js", "sys.js");

        sb.append("<script src=\"cordova.js\"></script>");
        sb.append("<script src=\"sys.js\"></script>");

        for (WebAppDependency dep : this.deps) {
            sb.append("<script src=\"").append(String.format("./%s.js", dep.code_bundle_hash)).append("\"></script>");
        }
        sb.append("<script src=\"").append(String.format("./%s.js", this.latest_version.code_bundle_hash)).append("\"></script>");

        sb.append("  </head>\n" +
                "  <body>\n" +
                "\n" +
                "  </body>\n" +
                "</html>\n");

        String html = sb.toString();
        String fileName = Hashing.sha1().hashString(html, Charsets.UTF_8).toString();
        File htmlFile = new File(Utils.getFilePath(context, fileName + ".html"));
        FileOutputStream fOut = new FileOutputStream(htmlFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(html);
        myOutWriter.close();
        fOut.flush();
        fOut.close();
        Utils.downloadFile(context, this.latest_version.code_bundle_hash, ".js");

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
