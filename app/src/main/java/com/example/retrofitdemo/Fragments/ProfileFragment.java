package com.example.retrofitdemo.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.retrofitdemo.Models.Follows;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.TwitchApi;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("display_name"));
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        TextView followCountText = (TextView) view.findViewById(R.id.follower_count);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/helix/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        TwitchApi twitchApi = retrofit.create(TwitchApi.class);

        Call<Follows> followsCall;
        Log.d("BREEZY", "USER_ID: " + getArguments().getString("user_id"));

        followsCall = twitchApi.getFollows(String.valueOf(getArguments().getString("user_id")));

        followsCall.enqueue(new Callback<Follows>() {
            @Override
            public void onResponse(Call<Follows> call, Response<Follows> response) {
                if (!response.isSuccessful()) {
                    Log.d("TAG", "FOLLOW CODE: " + response.code());
                    return;
                }
                Follows follows = response.body();
                Log.d("BREEZY", "Follower Count: " + follows.getTotal());
                followCountText.setText(String.valueOf(follows.getTotal()));


            }

            @Override
            public void onFailure(Call<Follows> call, Throwable t) {
                Log.d("TAG", " We not good!");

            }
        });

        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);
        Glide.with(getActivity())
                .asBitmap()
                .load(getArguments().getString("profile_image_url"))
                .into(circleImageView);

        TextView viewcountText = (TextView) view.findViewById(R.id.profile_view_count);
        viewcountText.setText(String.valueOf(getArguments().getInt("view_count")));

        TextView descriptionText = (TextView) view.findViewById(R.id.profile_description);
        descriptionText.setText(String.valueOf(getArguments().getString("description")));


    }

}
