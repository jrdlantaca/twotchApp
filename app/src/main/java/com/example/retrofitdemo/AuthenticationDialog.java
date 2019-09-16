package com.example.retrofitdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationDialog extends Dialog {
    private final AuthenticationListener listener;
    private Context context;
    private WebView webView;
    private String access_token;

    private String CLIENT_ID;


    private final String url = "https://id.twitch.tv/oauth2/authorize" +
            "?client_id=1qov2cmoknnwm9dlgc067uj7z0i5xf" +
            "&redirect_uri=https://analytics-demo-9982e.firebaseapp.com/__/auth/handler" +
            "&response_type=code" +
            "&scope=user:read:email+user:edit";
    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener){
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initializeWebView();
    }

    private void initializeWebView() {

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        Log.d("BREEZY", "URL: " + url);
        webView.setWebViewClient(new WebViewClient() {

            boolean authComplete = false;
            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
            }
            String auth_code;
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("?code=") && !authComplete) {
                    Uri uri = Uri.parse(url);
                    Log.d("BREEZY", "URI: " + uri.toString());
                    auth_code = uri.getQueryParameter("code");
                    // get the whole token after the '=' sign
                    //access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                    Log.d("BREEZY", "AUTH_CODE: " + auth_code);



                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://id.twitch.tv/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AccessTokenService accessTokenService = retrofit.create(AccessTokenService.class);
                    Call<TokenResponse> call = accessTokenService.getToken("1qov2cmoknnwm9dlgc067uj7z0i5xf",
                            "p86skmpy16b6rcc5rycoaeehenxf87",
                            auth_code,
                            "authorization_code",
                            "https://analytics-demo-9982e.firebaseapp.com/__/auth/handler");



                    call.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            Log.d("BREEZY", "RESPONSE: " + response.body());
                            if(!response.isSuccessful()){
                                Log.d("BREEZY", "RESPONSE CODE: " + response.code());
                            }
                            access_token = response.body().getAccess_token();
                            Log.d("BREEZY", "ACCESS_TOKEN IN CALL: " + access_token);
                            Log.d("BREEZY", "EXPIRES_IN: " + response.body().getExpires_in());
                            Log.d("BREEZY", "REFRESH_TOKEN: " + response.body().getRefresh_token());
                            Log.d("BREEZY", "SCOPE: " + response.body().getScope());
                            Log.d("BREEZY", "ACCESS_TOKEN BEFORE LISTENER: " + access_token );

                            authComplete = true;
                            listener.onCodeReceived(access_token);
                            dismiss();

                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                                Log.d("BREEZY", "WE NOT GOOD");
                        }
                    });

                }
                else if (url.contains("?error")){
                    Log.d("BREEZY", "URL: " + url);
                    Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }
}
