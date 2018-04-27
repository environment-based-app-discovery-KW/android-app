package com.appdiscovery.app;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private WebApp[] webApps;

    public AppListAdapter(WebApp[] webApps) {
        this.webApps = webApps;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("onBindViewHolder", String.valueOf(position));
        TextView appTitleText = holder.mView.findViewById(R.id.appName);
        appTitleText.setText(webApps[position].name);
//        holder.mView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return webApps.length;
    }
}
