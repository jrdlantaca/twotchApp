package com.example.retrofitdemo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopGames {

    @SerializedName("data")
    private List<TopGame> games;

    public List<TopGame> getTopGames() { return games;}

    public class TopGame {
        private String name;

        public void setBox_art_url(String box_art_url) {
            this.box_art_url = box_art_url;
        }

        private String box_art_url;

        public String getName() {
            return name;
        }

        public String getBox_art_url() {
            return box_art_url;
        }
    }
}
