package com.codecool.backend.controller;

import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.service.CharacterOptionService;
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
}