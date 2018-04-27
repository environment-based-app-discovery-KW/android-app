package com.appdiscovery.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.appdiscovery.app.services.DiscoverApp;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    MainActivity() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private View.OnClickListener onListItemClick = (view -> {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        Log.d("itemClicked", String.valueOf(itemPosition));
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Config.getInstance().repoServerAddr + "/app/download?id=1")
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Gson gson = new Gson();
                WebApp app = gson.fromJson(jsonData, WebApp.class);
                app.setContext(MainActivity.this);
//                    Toast.makeText(MainActivity.this, app.name, Toast.LENGTH_SHORT).show();
                app.downloadAndRun();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                Intent myIntent = new Intent(MainActivity.this, WebViewActivity.class);
//                startActivity(myIntent);
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.apps_list);
        mRecyclerView.setHasFixedSize(true);

        DiscoverApp.byGpsLocation(this, webapps -> {
            // specify an adapter (see also next example)
            mAdapter = new AppListAdapter(webapps, onListItemClick);
            mRecyclerView.setAdapter(mAdapter);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            return "";
        });
    }

}
