package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.UserCheckpointPatchRequest;
import com.codecool.backend.model.User;
import com.codecool.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.USERS)
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @PatchMapping(ApiRoutes.USER_CHECKPOINT)
    public User updateCheckpoint(
            @PathVariable String id,
            @Valid @RequestBody UserCheckpointPatchRequest body
    ) {
        return service.updateCheckpoint(id, body.checkpoint());
    }
}