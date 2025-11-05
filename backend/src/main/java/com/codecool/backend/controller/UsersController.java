package com.codecool.backend.controller;

import com.codecool.backend.dto.UserCreateRequest;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository repository;

    public UsersController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserCreateRequest body) {
        User u = new User();
        u.setName(body.name());
        u.setCheckpoint(body.checkpoint() != null ? body.checkpoint() : 1);
        u.setCharacter(body.character());
        User saved = repository.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
