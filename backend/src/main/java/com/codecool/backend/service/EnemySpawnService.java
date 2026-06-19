package com.codecool.backend.service;

import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class EnemySpawnService {

    private static final int MIN_ENEMY_Y = 80;
    private static final int MAX_ENEMY_Y = 420;

    private static final int MIN_PLATFORM_WIDTH_FOR_ENEMY = 90;
    private static final int ENEMY_SPAWN_CHANCE_PERCENT = 35;

    private static final int FIRST_RANDOM_PLATFORM_INDEX = 1;
    private static final int MIN_ENEMY_COUNT = 1;
    private static final int MAX_ENEMY_COUNT = 5;
    private static final int PLATFORM_COUNT_PER_ENEMY = 6;

    private final EnemyRepository enemyRepository;
    private final Random random = new Random();

    public EnemySpawnService(EnemyRepository enemyRepository) {
        this.enemyRepository = enemyRepository;
    }

    public List<EnemySpawnDto> generateEnemies(List<PlatformDto> platforms) {
        List<Enemy> enemyTypes = getEnemyTypes();

        if (enemyTypes.isEmpty()) {
            return List.of();
        }

        return platforms.stream()
                .skip(FIRST_RANDOM_PLATFORM_INDEX)
                .filter(platform -> platform.width() >= MIN_PLATFORM_WIDTH_FOR_ENEMY)
                .filter(this::isSafeEnemyPlatform)
                .filter(platform -> random.nextInt(100) < ENEMY_SPAWN_CHANCE_PERCENT)
                .limit(calculateEnemyCount(platforms))
                .map(platform -> createEnemySpawn(platform, enemyTypes))
                .toList();
    }

    private EnemySpawnDto createEnemySpawn(
            PlatformDto platform,
            List<Enemy> enemyTypes
    ) {
        Enemy enemyType = enemyTypes.get(random.nextInt(enemyTypes.size()));

        return new EnemySpawnDto(
                platform.x() + platform.width() / 2 - GameConstants.ENEMY_WIDTH / 2,
                platform.y() - GameConstants.ENEMY_HEIGHT,
                GameConstants.ENEMY_WIDTH,
                GameConstants.ENEMY_HEIGHT,
                enemyType.getSpeed(),
                enemyType.getDamage(),
                enemyType.getHp(),
                enemyType.getColor(),
                enemyType.isCanJump()
        );
    }

    private List<Enemy> getEnemyTypes() {
        return enemyRepository.findAll();
    }

    private boolean isSafeEnemyPlatform(PlatformDto platform) {
        int enemyY = platform.y() - GameConstants.ENEMY_HEIGHT;

        return enemyY >= MIN_ENEMY_Y && enemyY <= MAX_ENEMY_Y;
    }

    private int calculateEnemyCount(List<PlatformDto> platforms) {
        return Math.max(
                MIN_ENEMY_COUNT,
                Math.min(MAX_ENEMY_COUNT, platforms.size() / PLATFORM_COUNT_PER_ENEMY)
        );
    }
}