package com.example.retrofitdemo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Streams {


    @SerializedName("data")
    private List<Stream> streams;


    public List<Stream> getStreams(){
        return streams;
    }


    public class Stream {
        private String user_id;
        private String user_name;
        @SerializedName("type")
        private String onlineStatus;
        private String viewer_count;
        private String thumbnail_url;
        private String game_id;
        private String title;

        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }

        public String getViewer_count() {
            return viewer_count;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getGame_id() {
            return game_id;
        }

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public String getTitle() {
            return title;
        }


    }
}
