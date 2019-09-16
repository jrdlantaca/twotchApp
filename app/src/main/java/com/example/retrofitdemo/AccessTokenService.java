package com.example.retrofitdemo;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccessTokenService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<TokenResponse> getToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("code") String code,
            @Field("grant_type") String grant_type,
            @Field("redirect_uri") String redirect_uri
    );
}
