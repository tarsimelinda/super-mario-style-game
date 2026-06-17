package com.codecool.backend.controller;

import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.service.EnemyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enemies")
public class AdminEnemyController {

    private final EnemyService service;

    public AdminEnemyController(EnemyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enemy create(@Valid @RequestBody EnemyCreateRequest body) {
        return service.create(body);
    }
}