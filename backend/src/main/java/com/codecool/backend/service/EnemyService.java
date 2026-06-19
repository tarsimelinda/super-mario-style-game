package com.codecool.backend.service;

import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.dto.EnemyResponse;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.springframework.stereotype.Service;

@Service
public class EnemyService {

    private final EnemyRepository repository;

    public EnemyService(EnemyRepository repository) {
        this.repository = repository;
    }

    public EnemyResponse create(EnemyCreateRequest body) {
        Enemy enemy = new Enemy();
        enemy.setDamage(body.damage());
        enemy.setSpeed(body.speed());
        enemy.setHp(body.hp());
        enemy.setColor(body.color().trim());
        enemy.setCanJump(Boolean.TRUE.equals(body.canJump()));

        return toResponse(repository.save(enemy));
    }

    private EnemyResponse toResponse(Enemy enemy) {
        return new EnemyResponse(
                enemy.getId(),
                enemy.getDamage(),
                enemy.getSpeed(),
                enemy.getHp(),
                enemy.getColor(),
                enemy.isCanJump()
        );
    }
}