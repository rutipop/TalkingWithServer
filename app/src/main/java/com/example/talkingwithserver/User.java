package com.example.talkingwithserver;

public class User {

    public String username;
    public String pretty_name;
    public String image_url;

    public User(String user, String pretty, String imageUrl) {
        username = user;
        pretty_name = pretty;
        image_url = imageUrl;
    }

    public User() {
    }
}
