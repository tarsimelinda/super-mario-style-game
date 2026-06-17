package com.codecool.backend.service;

import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.springframework.stereotype.Service;

@Service
public class EnemyService {

    private final EnemyRepository repository;

    public EnemyService(EnemyRepository repository) {
        this.repository = repository;
    }

    public Enemy create(EnemyCreateRequest body) {
        Enemy e = new Enemy();
        e.setDamage(body.damage());
        e.setSpeed(body.speed());
        e.setHp(body.hp());
        e.setColor(body.color());
        e.setCanJump(Boolean.TRUE.equals(body.canJump()));

        return repository.save(e);
    }
}