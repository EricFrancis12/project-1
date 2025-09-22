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
import com.example.demo.service.UserService;

@RestController
public class Controller {

    private final UserService userService;

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> handleLogin(@RequestBody UserLoginInfo userLogin) {
        if (!userLogin.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            User user = userService.getUserByUsernameAndPassword(
                    userLogin.getUsername(), userLogin.getPassword());
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
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

    @RequestMapping("/**")
    public ResponseEntity<String> handleCatchAll() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Route not found");
    }
}
