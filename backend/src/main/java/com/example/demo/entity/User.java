package com.example.demo.entity;

import com.example.demo.util.HashingUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, length = 60)
    private String hashedPassword;

    public static int MAX_USERNAME_LENGTH = 64;
    public static int MIN_PASSWORD_LENGTH = 8;
    public static int MAX_PASSWORD_LENGTH = 64;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
    }

    // TODO: should we have a HashedPassword class?
    public static String hashPassword(String password) {
        return HashingUtil.hashSHA256(password);
    }

}
