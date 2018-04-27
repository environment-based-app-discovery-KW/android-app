package com.appdiscovery.app.services;

import android.content.Context;

import com.appdiscovery.app.Utils;
import com.appdiscovery.app.WebAppDependency;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AppBuilder {
    final static String copyFinishedFlag = ".asset_copy_finished";

    /**
     * 把载入时需要用到的assets拷到web目录
     */
    private static void copyAssets(Context context) throws IOException {
        if (new File(Utils.getFilePath(context, copyFinishedFlag)).exists()) {
            return;
        }
        Utils.copyAssetFileOrDir(context, "www/cordova.js", "cordova.js");
        Utils.copyAssetFileOrDir(context, "www/cordova_plugins.js", "cordova_plugins.js");
        Utils.copyAssetFileOrDir(context, "www/plugins", "plugins");
        Utils.copyAssetFileOrDir(context, "www/cordova-js-src", "cordova-js-src");
        Utils.copyAssetFileOrDir(context, "www/sys.js", "sys.js");
        new File(Utils.getFilePath(context, copyFinishedFlag)).createNewFile();
    }

    public static File build(Context context, WebAppDependency[] deps, String code_bundle_hash) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\" dir=\"ltr\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title></title>\n"
        );

        AppBuilder.copyAssets(context);

        sb.append("<script src=\"cordova.js\"></script>");
        sb.append("<script src=\"sys.js\"></script>");

        for (WebAppDependency dep : deps) {
            sb.append("<script src=\"").append(String.format("./%s.js", dep.code_bundle_hash)).append("\"></script>");
        }
        sb.append("<script src=\"").append(String.format("./%s.js", code_bundle_hash)).append("\"></script>");

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
        return htmlFile;
    }
}
