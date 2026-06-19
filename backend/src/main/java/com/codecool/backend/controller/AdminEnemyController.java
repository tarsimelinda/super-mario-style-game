package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.dto.EnemyResponse;
import com.codecool.backend.service.EnemyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.ADMIN_ENEMIES)
public class AdminEnemyController {

    private final EnemyService service;

    public AdminEnemyController(EnemyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnemyResponse create(@Valid @RequestBody EnemyCreateRequest body) {
        return service.create(body);
    }
}