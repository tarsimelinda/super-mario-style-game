package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.CharacterOptionResponse;
import com.codecool.backend.service.CharacterOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoutes.CHARACTERS)
public class CharacterOptionController {

    private final CharacterOptionService service;

    public CharacterOptionController(CharacterOptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<CharacterOptionResponse> getAll() {
        return service.getAll();
    }
}