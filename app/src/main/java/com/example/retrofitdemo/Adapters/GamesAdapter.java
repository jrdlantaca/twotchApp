package com.example.retrofitdemo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.retrofitdemo.Models.Games;
import com.example.retrofitdemo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Games.Game> gameList;
    private Context mContext;

    public GamesAdapter(ArrayList<Games.Game> gameList, Context mContext) {
        this.gameList = gameList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.box_art_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        String url = gameList.get(i).getBox_art_url();
        Log.d("BREEZY", "URL BEFORE REPLACING: " + url);
        url = url.replace("{width}", "128");
        url = url.replace("{height}", "128");
        gameList.get(i).setBox_art_url(url);
        Log.d("BREEZY", "URL AFTER REPLACING: " + url);
        Glide.with(mContext)
                .asBitmap()
                .load(gameList.get(i).getBox_art_url())
                .into(viewHolder.gameImg);

        viewHolder.gameName.setText(gameList.get(i).getName());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean twitchInstalled = isPackageInstalled("tv.twitch.android.app", mContext);
                if (twitchInstalled == false){
                    Toast.makeText(mContext, "Twitch is not installed in this device.", Toast.LENGTH_SHORT);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("twitch://game/" + gameList.get(i).getName()));
                    intent.setPackage("tv.twitch.android.app");
                    mContext.startActivity(intent);
                }

                /*
                Log.d(TAG, "onClick: clicked on: " + dataList.get(i).getUser_name());

                Toast.makeText(mContext, dataList.get(i).getUser_name(), Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView gameImg;
        CardView cardView;
        TextView gameName;
        public ViewHolder(View itemView){
            super(itemView);
            gameName = itemView.findViewById(R.id.game_name_text);
            gameImg = itemView.findViewById(R.id.box_art_img);
            cardView = itemView.findViewById(R.id.game_card_view);
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
