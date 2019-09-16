package com.example.retrofitdemo;



import com.example.retrofitdemo.Models.Follows;
import com.example.retrofitdemo.Models.Games;
import com.example.retrofitdemo.Models.Streams;
import com.example.retrofitdemo.Models.TopGames;
import com.example.retrofitdemo.Models.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TwitchApi {

    @Headers({
            "Client-ID: 1qov2cmoknnwm9dlgc067uj7z0i5xf",
    })

    @GET("games")
    Call<Games> getGames(@Query("name") String name);


    @Headers({
            "Client-ID: 1qov2cmoknnwm9dlgc067uj7z0i5xf",
    })

    @GET("streams")
    Call<Streams> getStreams();

    @Headers({
            "Client-ID: 1qov2cmoknnwm9dlgc067uj7z0i5xf",
    })

    @GET("games/top")
    Call<TopGames> getTopGames();


    @Headers({
            "Client-ID: 1qov2cmoknnwm9dlgc067uj7z0i5xf",
    })
    @GET("users")
    Call<Users> getUsers();

    @Headers({
            "Client-ID: 1qov2cmoknnwm9dlgc067uj7z0i5xf",
    })
    @GET("users/follows")
    Call<Follows> getFollows(@Query("to_id") String to_id);


}
