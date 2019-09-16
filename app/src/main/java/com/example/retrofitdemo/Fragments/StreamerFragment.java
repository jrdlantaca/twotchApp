package com.example.retrofitdemo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.retrofitdemo.Adapters.StreamerAdapter;
import com.example.retrofitdemo.Models.Streams;
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

public class StreamerFragment extends Fragment {
    private ArrayList<Streams.Stream> streamData;
    private Call<Streams> streamsCall;
    private TwitchApi twitchApi;
    private View view;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        view = inflater.inflate(R.layout.streamer_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.streamer_progress_bar);
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

        streamsCall = twitchApi.getStreams();
        streamsCall.enqueue(new Callback<Streams>() {
            @Override
            public void onResponse(Call<Streams> call, Response<Streams> response) {

                if(!response.isSuccessful()) {
                    Log.d("TAG", "CODE: " + response.code());
                    return;
                }

                Streams streams = response.body();
                streamData = new ArrayList<>(streams.getStreams());
                initRecyclerView(streamData);

            }

            @Override
            public void onFailure(Call<Streams> call, Throwable t) {
                Log.d("TAG", " We not good!");
            }
        });
    }

    private void initRecyclerView(ArrayList<Streams.Stream> dataList) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        /*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        */
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        StreamerAdapter adapter = new StreamerAdapter(streamData, getActivity());
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);


    }



}
