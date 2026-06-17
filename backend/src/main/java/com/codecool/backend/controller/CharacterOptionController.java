package com.codecool.backend.controller;

import com.codecool.backend.dto.CharacterCreateRequest;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.service.CharacterOptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterOptionController {

    private final CharacterOptionService service;

    public CharacterOptionController(CharacterOptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<CharacterOption> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterOption create(@Valid @RequestBody CharacterCreateRequest body) {
        return service.create(body);
    }
}