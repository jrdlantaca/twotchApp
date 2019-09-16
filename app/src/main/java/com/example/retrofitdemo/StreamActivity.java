package com.example.retrofitdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class StreamActivity extends AppCompatActivity {

    WebView mWebView, chatView;
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.stream_activity);

        intent = getIntent();
        String channel = intent.getStringExtra("channel");
        Log.d("BREEZY", "CHANNEL NAME: " + channel);

        mWebView = (WebView) findViewById(R.id.twitch_stream);
        chatView = (WebView) findViewById(R.id.twitch_chat);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        WebSettings chatSettings = chatView.getSettings();
        chatSettings.setJavaScriptEnabled(true);
        chatSettings.setUseWideViewPort(true);
        chatSettings.setLoadWithOverviewMode(true);

        mWebView.loadUrl("https://player.twitch.tv/?channel="+ channel);
        chatView.loadUrl("https://www.twitch.tv/embed/"+channel+"/chat");

    }
}
