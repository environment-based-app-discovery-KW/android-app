package com.appdiscovery.app;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private final View.OnClickListener mOnClickListener;
    private WebApp[] webApps;

    public AppListAdapter(WebApp[] webApps, View.OnClickListener onClickListener) {
        this.webApps = webApps;
        this.mOnClickListener = onClickListener;
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
        v.setOnClickListener(mOnClickListener);
        return new ViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("onBindViewHolder", String.valueOf(position));
        TextView appTitleText = holder.mView.findViewById(R.id.appName);
        appTitleText.setText(webApps[position].name);
        TextView appDescText = holder.mView.findViewById(R.id.appDesc);
        appDescText.setText(String.format("距离您 %.2f m", webApps[position].distance_in_m));
//        holder.mView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return webApps.length;
    }
}
