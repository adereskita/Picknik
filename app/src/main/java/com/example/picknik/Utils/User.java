package com.example.picknik.Utils;


public class User {
    //add user
    private String user_id,username, email, password, avatar, description, location, phone;
    //add place


    public User() {
    }


    //for input Data add User place or event

    public User(String avatar) {
        this.avatar = avatar;
    }


    //maybe get User Info
    public User(String user_id, String username, String phone, String location, String description, String avatar) {
        this.user_id = user_id;
        this.username = username;
        this.phone = phone;
        this.location = location;
        this.description = description;
        this.avatar = avatar;
    }


    //adding User
    public User(String user_id, String username, String email, String password) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

