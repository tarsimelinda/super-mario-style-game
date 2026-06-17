package com.codecool.backend.controller;

import com.codecool.backend.dto.CharacterCreateRequest;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.service.CharacterOptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/characters")
public class AdminCharacterController {

    private final CharacterOptionService service;

    public AdminCharacterController(CharacterOptionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterOption create(@Valid @RequestBody CharacterCreateRequest body) {
        return service.create(body);
    }
}