package com.codecool.backend.controller;

import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EnemyController {

    private final EnemyRepository repository;

    public EnemyController(EnemyRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/enemy")
    public List<Enemy> getAll() {
        return repository.findAll();
    }

    @PostMapping("/enemy/post")
    public Enemy create(@RequestBody Enemy enemy) {
        return repository.save(enemy);
    }

    @DeleteMapping("/enemy/delete")
    public String deleteZeroHp() {
        repository.deleteAll(repository.findAll().stream()
                .filter(e -> e.getHp() == 0)
                .toList());
        return "Enemies with 0 HP deleted.";
    }
}
