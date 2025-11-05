package com.codecool.backend.controller;

import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enemies")
public class EnemiesController {

    private final EnemyRepository repository;

    public EnemiesController(EnemyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Enemy> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Enemy> create(@Valid @RequestBody EnemyCreateRequest body) {
        Enemy e = new Enemy();
        e.setDamage(body.damage());
        e.setSpeed(body.speed());
        e.setHp(body.hp());
        e.setColor(body.color());
        e.setCanJump(Boolean.TRUE.equals(body.canJump()));
        Enemy saved = repository.save(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/zero-hp")
    public ResponseEntity<String> deleteZeroHp() {
        long deleted = repository.deleteByHp(0);
        return ResponseEntity.ok("Deleted enemies: " + deleted);
    }
}
