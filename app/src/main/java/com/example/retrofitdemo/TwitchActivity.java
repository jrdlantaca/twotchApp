package com.example.retrofitdemo;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.retrofitdemo.Adapters.GamesAdapter;
import com.example.retrofitdemo.Adapters.StreamerAdapter;
import com.example.retrofitdemo.Adapters.TopGameAdapter;
import com.example.retrofitdemo.Fragments.ProfileFragment;
import com.example.retrofitdemo.Fragments.SearchFragment;
import com.example.retrofitdemo.Fragments.StreamerFragment;
import com.example.retrofitdemo.Fragments.TopGamesFragment;
import com.example.retrofitdemo.Models.Games;
import com.example.retrofitdemo.Models.Streams;
import com.example.retrofitdemo.Models.TopGames;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "TAG";
    private DrawerLayout drawer;


    private ArrayList<Games.Game> gameData;
    private ArrayList<TopGames.TopGame> topGamesList;



    private Call<TopGames> topgamesCall;

    private Call<Games> gamesCall;


    private String access_token = "";
    private String profile_image_url= "";
    private String email = "";
    private String displayName = "";
    private String description = "";
    private int view_count;
    private String user_id;

    private RecyclerView recyclerView, TopGamesView, GamesView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent i = this.getIntent();
        access_token = i.getStringExtra("access_token");
        profile_image_url = i.getStringExtra("profile_image_url");
        email = i.getStringExtra("email");
        user_id = i.getStringExtra("user_id");
        displayName = i.getStringExtra("displayName");
        description = i.getStringExtra("description");
        view_count = i.getIntExtra("view_count", 0);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        CircleImageView profileImg = view.findViewById(R.id.nav_profile_img);

        TextView navLogin = view.findViewById(R.id.nav_login_name);
        navLogin.setText(displayName);

        TextView navEmail = view.findViewById(R.id.nav_email);
        navEmail.setText(email);

        Glide.with(this)
                .asBitmap()
                .load(profile_image_url)
                .into(profileImg);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StreamerFragment()).commit();



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_profile_button:
                Bundle bundle = new Bundle();
                bundle.putString("profile_image_url", profile_image_url);
                bundle.putString("email", email);
                bundle.putString("display_name",displayName);
                bundle.putString("description", description);
                bundle.putInt("view_count", view_count);
                bundle.putString("user_id",user_id);
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new StreamerFragment()).commit();
        }
        if(currentFragment instanceof StreamerFragment){
            super.onBackPressed();
        }
        else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new StreamerFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_streams:
                            selectedFragment = new StreamerFragment();
                            break;
                        case R.id.nav_games:
                            selectedFragment = new TopGamesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.searched_game:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
