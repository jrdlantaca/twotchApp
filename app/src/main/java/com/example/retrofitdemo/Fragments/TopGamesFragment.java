package com.example.retrofitdemo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.example.retrofitdemo.Adapters.TopGameAdapter;

import com.example.retrofitdemo.Models.TopGames;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.TwitchApi;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopGamesFragment extends Fragment {
    private ArrayList<TopGames.TopGame> topGamesList;
    private Call<TopGames> gamesCall;
    private TwitchApi twitchApi;
    private ProgressBar progressBar;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        view = inflater.inflate(R.layout.topgames_fragment, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.topgames_progress_bar);
        progressBar.setVisibility(View.VISIBLE);


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


        twitchApi = retrofit.create(TwitchApi.class);

        gamesCall = twitchApi.getTopGames();
        gamesCall.enqueue(new Callback<TopGames>() {
            @Override
            public void onResponse(Call<TopGames> call, Response<TopGames> response) {

                if(!response.isSuccessful()) {
                    Log.d("TAG", "CODE: " + response.code());
                    return;
                }

                TopGames topGames = response.body();
                topGamesList = new ArrayList<>(topGames.getTopGames());
                initRecyclerView(topGamesList);
            }

            @Override
            public void onFailure(Call<TopGames> call, Throwable t) {
                Log.d("TAG", " We not good!");
            }
        });
    }

    private void initRecyclerView(ArrayList<TopGames.TopGame> dataList) {
        Log.d("BREEZY", "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = view.findViewById(R.id.topgamesview);
        /*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        */
        TopGameAdapter adapter = new TopGameAdapter(dataList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar.setVisibility(View.INVISIBLE);

    }
}
