package com.example.demo.dto;

import com.example.demo.entity.User;

public class UserInfo {

    private String username;
    private String password;

    public boolean isValid() {
        return !username.isEmpty()
                && username.length() <= User.MAX_USERNAME_LENGTH
                && password.length() >= User.MIN_PASSWORD_LENGTH
                && password.length() <= User.MAX_PASSWORD_LENGTH;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User toUser() {
        return new User(username, password);
    }

}
