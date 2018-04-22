package com.appdiscovery.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import io.cordova.hellocordova.WebViewActivity;
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

    public void downloadAndRun() throws IOException {
        //TODO: cache
        String appCodeFile = Utils.getInstance().downloadFile(this.context, this.latestVersion.code_bundle_hash, ".js");
        ArrayList<String> depsCodeFiles = new ArrayList<>();
        for (WebAppDependency dep : this.deps) {
            depsCodeFiles.add(Utils.getInstance().downloadFile(this.context, dep.code_bundle_hash, ".js"));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\" dir=\"ltr\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title></title>\n"
        );

        for (WebAppDependency dep : this.deps) {
            sb.append("<script src=\"").append(String.format("./%s.js", dep.code_bundle_hash)).append("\"></script>");
        }
        sb.append("<script src=\"").append(String.format("./%s.js", this.latestVersion.code_bundle_hash)).append("\"></script>");

        sb.append("  </head>\n" +
                "  <body>aaaaaaaaa\n" +
                "\n" +
                "  </body>\n" +
                "</html>\n");

        String html = sb.toString();
        String fileName = Hashing.sha1().hashString(html, Charsets.UTF_8).toString();
        File htmlFile = new File(context.getCacheDir(), fileName + ".html");
        FileOutputStream fOut = new FileOutputStream(htmlFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(html);
        myOutWriter.close();
        fOut.flush();
        fOut.close();

        Log.d("WebAppBuild", htmlFile.getAbsolutePath());
        Intent myIntent = new Intent(context, WebViewActivity.class);
        myIntent.putExtra("fileName", htmlFile.getAbsolutePath());
        context.startActivity(myIntent);
    }
}
