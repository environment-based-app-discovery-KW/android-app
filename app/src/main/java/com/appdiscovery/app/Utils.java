package com.appdiscovery.app;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class Utils {
    private static final Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public String getFilePath(Context context, String hashAndExt) {
        return context.getCacheDir() + "/" + hashAndExt;
    }

    public String downloadFile(Context context, String hash, String ext) {
        String filePath = getFilePath(context, hash + ext);
        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            return downloadedFile.getAbsolutePath();
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.getInstance().serverAddr + "/file/download?hash=" + hash)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();
            return downloadedFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void copyAssetFileOrDir(Context context, String from, String to) throws IOException {
        AssetManager assetManager = context.getAssets();
        String assets[] = null;
        assets = assetManager.list(from);
        if (assets.length == 0) {
            InputStream assetInputStream = assetManager.open(from);
            FileOutputStream outStream = new FileOutputStream(Utils.getInstance().getFilePath(context, to));
            ByteStreams.copy(assetInputStream, outStream);
            outStream.flush();
            outStream.close();
            assetInputStream.close();
        } else {
            String fullPath = Utils.getInstance().getFilePath(context, to);
            File dir = new File(fullPath);
            if (!dir.exists())
                dir.mkdir();
            for (int i = 0; i < assets.length; ++i) {
                copyAssetFileOrDir(context, from + "/" + assets[i], to + "/" + assets[i]);
            }
        }

    }

}
