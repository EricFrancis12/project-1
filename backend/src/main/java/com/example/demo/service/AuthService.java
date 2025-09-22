package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface AuthService {
    void verify(HttpServletRequest request) throws UnauthorizedException;
}
