package com.example.retrofitdemo.Fragments;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;

import com.example.retrofitdemo.Adapters.GamesAdapter;
import com.example.retrofitdemo.Adapters.StreamerAdapter;
import com.example.retrofitdemo.Models.Games;
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

public class SearchFragment extends Fragment {

    private ArrayList<Games.Game> gamesData;
    private Call<Games> gamesCall;
    private TwitchApi twitchApi;
    private View view;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.search_fragment, container, false);
        progressBar = view.findViewById(R.id.search_progress_bar);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_menu, menu);
        MenuItem item = menu.findItem(R.id.game_search_button);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.onActionViewExpanded();
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                gamesCall = twitchApi.getGames(query);
                gamesCall.enqueue(new Callback<Games>() {
                    @Override
                    public void onResponse(Call<Games> call, Response<Games> response) {
                        if (!response.isSuccessful()) {
                            Log.d("TAG", "CODE: " + response.code());
                            return;
                        }
                        Games games = response.body();
                        gamesData = new ArrayList<>(games.getGames());
                        for (int i = 0; i < gamesData.size(); i++) {
                            Log.d("BREEZY", "BOX ART: " + gamesData.get(i).getBox_art_url());
                        }
                        initSearchGameView(gamesData);

                    }

                    @Override
                    public void onFailure(Call<Games> call, Throwable t) {
                        Log.d("TAG", " We not good!");
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSearchGameView(ArrayList<Games.Game> gamesData) {
        RecyclerView recyclerView = view.findViewById(R.id.games_recycler_view);
        /*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        */
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GamesAdapter adapter = new GamesAdapter(gamesData, getActivity());
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);

    }
}




