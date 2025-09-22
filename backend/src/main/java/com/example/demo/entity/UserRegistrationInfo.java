package com.example.demo.entity;

public class UserRegistrationInfo {

    private String username;
    private String password;

    public boolean isValid() {
        // TODO: ...
        return true;
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
