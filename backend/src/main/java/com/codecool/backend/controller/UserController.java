package com.codecool.backend.controller;

import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @PostMapping("/user/post")
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }
}
