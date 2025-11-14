package com.codecool.backend.controller;

import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.service.EnemyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enemies")
public class EnemiesController {

    private final EnemyService service;

    public EnemiesController(EnemyService service) {
        this.service = service;
    }

    @GetMapping
    public List<Enemy> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enemy create(@Valid @RequestBody EnemyCreateRequest body) {
        return service.create(body);
    }

    @DeleteMapping("/zero-hp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteZeroHp() {
        service.deleteZeroHp();
    }
}
