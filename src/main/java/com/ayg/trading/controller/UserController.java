package com.ayg.trading.controller;

import com.ayg.trading.model.User;
import com.ayg.trading.repository.UserRepository;
import com.ayg.trading.service.implemetations.UserServiceImplementation;
import com.ayg.trading.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
       return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        return userService.login(user);
    }

}
