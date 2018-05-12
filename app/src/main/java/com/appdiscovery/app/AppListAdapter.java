package com.appdiscovery.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

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
                .inflate(R.layout.app_list_item, parent, false);
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
        new DownloadImageTask(holder.mView.findViewById(R.id.app_image_view))
                .execute(webApps[position].latest_version.logo_url);
        if (webApps[position].distance_in_m < 0) {
            appDescText.setText("附近的置顶APP");
            appDescText.setTextColor(Color.rgb(255, 200, 0));
        } else {
            appDescText.setText(String.format("距离您 %.2f m", webApps[position].distance_in_m));
        }
//        holder.mView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return webApps.length;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
