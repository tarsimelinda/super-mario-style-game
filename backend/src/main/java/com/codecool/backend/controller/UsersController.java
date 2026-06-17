package com.codecool.backend.controller;

import com.codecool.backend.dto.UserCheckpointPatchRequest;
import com.codecool.backend.dto.UserCreateRequest;
import com.codecool.backend.model.User;
import com.codecool.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserCreateRequest body) {
        return service.create(body);
    }

    @PatchMapping("/{id}/checkpoint")
    public User updateCheckpoint(
            @PathVariable String id,
            @Valid @RequestBody UserCheckpointPatchRequest body
    ) {
        return service.updateCheckpoint(id, body.checkpoint());
    }
}