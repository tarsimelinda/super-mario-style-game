package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.CharacterCreateRequest;
import com.codecool.backend.dto.CharacterOptionResponse;
import com.codecool.backend.service.CharacterOptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.ADMIN_CHARACTERS)
public class AdminCharacterController {

    private final CharacterOptionService service;

    public AdminCharacterController(CharacterOptionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterOptionResponse create(@Valid @RequestBody CharacterCreateRequest body) {
        return service.create(body);
    }
}