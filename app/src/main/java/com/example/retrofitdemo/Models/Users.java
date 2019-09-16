package com.example.retrofitdemo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Users {
    @SerializedName("data")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public class User {
        private String display_name;

        public String getDisplay_name() {
            return display_name;
        }

        public int getView_count() {
            return view_count;
        }

        public String getOffline_image_url() {
            return offline_image_url;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        private String id;
        private int view_count;
        private String offline_image_url;
        private String profile_image_url;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        private String email;

        public void setOffline_image_url(String offline_image_url) {
            this.offline_image_url = offline_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        private String description;
    }
}
