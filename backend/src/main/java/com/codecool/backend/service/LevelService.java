package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class LevelService {

    private static final int RANDOM_LEVEL_ATTEMPTS = 50;

    private static final int ENEMY_WIDTH = 40;
    private static final int ENEMY_HEIGHT = 40;
    private static final int MIN_ENEMY_Y = 80;
    private static final int MAX_ENEMY_Y = 420;

    private final PlatformGeneratorService platformGeneratorService;
    private final CoinGeneratorService coinGeneratorService;
    private final LevelValidatorService levelValidatorService;
    private final LevelDefaultsService levelDefaultsService;
    private final EnemyRepository enemyRepository;

    private final Random random = new Random();

    public LevelService(
            PlatformGeneratorService platformGeneratorService,
            CoinGeneratorService coinGeneratorService,
            LevelValidatorService levelValidatorService,
            LevelDefaultsService levelDefaultsService,
            EnemyRepository enemyRepository
    ) {
        this.platformGeneratorService = platformGeneratorService;
        this.coinGeneratorService = coinGeneratorService;
        this.levelValidatorService = levelValidatorService;
        this.levelDefaultsService = levelDefaultsService;
        this.enemyRepository = enemyRepository;
    }

    public LevelDto getDefaultLevel() {
        return levelDefaultsService.getDefaultLevel();
    }

    public LevelDto getRandomLevel() {
        for (int attempt = 0; attempt < RANDOM_LEVEL_ATTEMPTS; attempt++) {
            List<PlatformDto> platforms = platformGeneratorService.generatePlatforms();

            if (!levelValidatorService.isPlayable(
                    LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                    platforms
            )) {
                continue;
            }

            List<CoinDto> coins = coinGeneratorService.generateCoins(platforms);
            List<EnemySpawnDto> enemies = generateEnemies(platforms);

            return new LevelDto(
                    LevelDefaultsService.RANDOM_LEVEL_PLAYER_START,
                    LevelDefaultsService.DEFAULT_LIVES,
                    platforms,
                    enemies,
                    coins
            );
        }

        return getDefaultLevel();
    }

    private List<EnemySpawnDto> generateEnemies(List<PlatformDto> platforms) {
        List<Enemy> enemyTypes = enemyRepository.findAll();

        if (enemyTypes.isEmpty()) {
            return List.of();
        }

        return platforms.stream()
                .skip(1)
                .filter(platform -> platform.width() >= 90)
                .filter(this::isSafeEnemyPlatform)
                .filter(platform -> random.nextInt(100) < 35)
                .limit(calculateEnemyCount(platforms))
                .map(platform -> {
                    Enemy enemyType = enemyTypes.get(random.nextInt(enemyTypes.size()));

                    return new EnemySpawnDto(
                            platform.x() + platform.width() / 2 - ENEMY_WIDTH / 2,
                            platform.y() - ENEMY_HEIGHT,
                            ENEMY_WIDTH,
                            ENEMY_HEIGHT,
                            enemyType.getSpeed(),
                            enemyType.getDamage(),
                            enemyType.getHp(),
                            enemyType.getColor(),
                            enemyType.isCanJump()
                    );
                })
                .toList();
    }

    private List<Enemy> getEnemyTypes() {
        return enemyRepository.findAll();
    }

    private boolean isSafeEnemyPlatform(PlatformDto platform) {
        int enemyY = platform.y() - ENEMY_HEIGHT;

        return enemyY >= MIN_ENEMY_Y && enemyY <= MAX_ENEMY_Y;
    }

    private int calculateEnemyCount(List<PlatformDto> platforms) {
        return Math.max(1, Math.min(5, platforms.size() / 6));
    }
}