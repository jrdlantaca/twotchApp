package com.example.retrofitdemo.Models;

import com.google.gson.annotations.SerializedName;

public class Follows {
    @SerializedName("total")
    private int total;

    public int getTotal() {
        return total;
    }
}
