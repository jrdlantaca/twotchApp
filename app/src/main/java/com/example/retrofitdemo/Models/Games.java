package com.example.retrofitdemo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Games {
    @SerializedName("data")
    private List<Game> games;

    public List<Game> getGames() { return games;}

    public class Game{
        private String id;
        private String box_art_url;
        private String name;

        public void setBox_art_url(String box_art_url) {
            this.box_art_url = box_art_url;
        }

        public String getId() {
            return id;
        }

        public String getBox_art_url() {
            return box_art_url;
        }

        public String getName() {
            return name;
        }
    }
}
