package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserLoginInfo;
import com.example.demo.dto.UserRegistrationInfo;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.middleware.Auth;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class Controller {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public Controller(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<User> handleRegister(@RequestBody UserRegistrationInfo userReg) {
        if (!userReg.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.usernameExists(userReg.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User createdUser = userService.registerUser(userReg);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/api/login")
    public ResponseEntity<User> handleLogin(
            @RequestBody UserLoginInfo userLogin, HttpServletResponse response) {
        if (!userLogin.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            User user = userService.getUserByUsernameAndPassword(
                    userLogin.getUsername(), userLogin.getPassword());
            authService.login(response, user);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping("/api/logout")
    public ResponseEntity<String> handleLogout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok("You have logged out");
    }

    @Auth
    @RequestMapping("/auth-check")
    public ResponseEntity<String> handleAuthCheck() {
        return ResponseEntity.ok("You are authenticated");
    }

    @RequestMapping("/**")
    public ResponseEntity<String> handleCatchAll() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Route not found");
    }

}
