package com.example.retrofitdemo;

import java.io.IOException;

public interface AuthenticationListener {
    void onCodeReceived(String access_token);
}
