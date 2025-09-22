package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    public void verify(HttpServletRequest request) throws UnauthorizedException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new UnauthorizedException("Missing Authorization header");
        }
        String[] parts = authHeader.trim().split("\\s+");
        if (parts.length != 2
                || !parts[0].equals("Bearer")
                || !parts[1].equals("TODO_CREATE_AUTH_TOKEN")) {
            throw new UnauthorizedException("Invalid Authorization header");
        }
    }

}
