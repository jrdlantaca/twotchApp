package com.example.retrofitdemo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.StreamActivity;
import com.example.retrofitdemo.Models.Streams;

import java.util.ArrayList;

public class StreamerAdapter extends RecyclerView.Adapter<StreamerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Streams.Stream> dataList;
    private Context mContext;

    public StreamerAdapter(ArrayList<Streams.Stream> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        String url = dataList.get(i).getThumbnail_url();
        url = url.replace("{width}", "128");
        url = url.replace("{height}", "128");
        dataList.get(i).setThumbnail_url(url);
        Glide.with(mContext)
                .asBitmap()
                .load(dataList.get(i).getThumbnail_url())
                .into(viewHolder.gameImg);


        viewHolder.streamerName.setText(dataList.get(i).getUser_name());
        viewHolder.title.setText(dataList.get(i).getTitle());
        viewHolder.viewerCount.setText("Playing for " + dataList.get(i).getViewer_count() + " viewers");

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StreamActivity.class);
                intent.putExtra("channel", dataList.get(i).getUser_name());
                mContext.startActivity(intent);

                /*
                boolean twitchInstalled = isPackageInstalled("tv.twitch.android.app", mContext);
                if (twitchInstalled == false){
                    Toast.makeText(mContext, "Twitch is not installed in this device.", Toast.LENGTH_SHORT);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("twitch://stream/" + dataList.get(i).getUser_name()));
                    intent.setPackage("tv.twitch.android.app");
                    mContext.startActivity(intent);
                }
                */
                /*
                Log.d(TAG, "onClick: clicked on: " + dataList.get(i).getUser_name());

                Toast.makeText(mContext, dataList.get(i).getUser_name(), Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView gameImg;
        TextView streamerName, title, viewerCount;
        CardView cardView;

        public ViewHolder(View itemView){
            super(itemView);
            gameImg = itemView.findViewById(R.id.gameImg);
            streamerName = itemView.findViewById(R.id.streamer_name);
            title = itemView.findViewById(R.id.stream_title);
            viewerCount = itemView.findViewById(R.id.viewer_count);
            cardView = itemView.findViewById(R.id.streamer_card_view);
        }

    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
