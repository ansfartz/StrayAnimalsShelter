package com.bcs.andy.strayanimalsshelter.models;

import java.util.ArrayList;

public class User {

    private String name;
    private String email;   // used to Login
    private String phone;
    private ArrayList<User> friends;

    public User() { }

    public User(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        friends = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
}
