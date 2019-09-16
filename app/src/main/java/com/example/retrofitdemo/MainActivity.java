package com.example.retrofitdemo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.retrofitdemo.Models.Users;

//import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Interceptor;
import okhttp3.MediaType;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements AuthenticationListener{


    private Button button;
    private AuthenticationDialog auth_dialog;
    private String uid;
    private String customToken;
    private String displayName;
    private String description, email;
    private int view_count;

    private OkHttpClient okHttpClient;
    private HttpLoggingInterceptor loggingInterceptor;

    private Call<Users> usersCall;
    private ArrayList<Users.User> usersList;
    private TwitchApi twitchApi;

    private String profile_image_url;
    private ProgressBar progressBar;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);

        button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                auth_dialog = new AuthenticationDialog(MainActivity.this, MainActivity.this);
                auth_dialog.setCancelable(true);
                auth_dialog.show();
            }
        });

    }


    @Override
    public void onCodeReceived(String access_token){
        if(access_token == null){
            auth_dialog.dismiss();
        }

        Log.d("BREEZY", "ACCESS_TOKEN BEFORE INTERCEPTOR: " + access_token);
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + access_token)
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/helix/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        twitchApi = retrofit.create(TwitchApi.class);


        usersCall = twitchApi.getUsers();

        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Log.d("BREEZY", "RESPONSE: " + response.body());
                if(!response.isSuccessful()){
                    Log.d("BREEZY", "CODE: " + response.code());
                }

                Users users = response.body();
                usersList = new ArrayList<>(users.getUsers());
                for (int i = 0; i < usersList.size();i++){
                    profile_image_url = usersList.get(0).getProfile_image_url();
                   Log.d("BREEZY", "USER_ID: " + usersList.get(i).getId());
                   uid = usersList.get(0).getId();
                   displayName = usersList.get(0).getDisplay_name();
                   email = usersList.get(0).getEmail();
                   description = usersList.get(0).getDescription();
                   view_count = usersList.get(0).getView_count();
                }
                Log.d("BREEZY", "PROFILE: " + profile_image_url);
                Intent i = new Intent(MainActivity.this, TwitchActivity.class);
                i.putExtra("user_id", uid);
                i.putExtra("access_token", access_token);
                i.putExtra("profile_image_url", profile_image_url);
                i.putExtra("email", email);
                i.putExtra("displayName", displayName);
                i.putExtra("description", description);
                i.putExtra("view_count", view_count);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(i);

            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                    Log.d("BREEZY", "WE NOT GOOD!");
            }
        });



    }

}
