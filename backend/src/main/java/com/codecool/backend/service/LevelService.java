package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {

    private static final int RANDOM_LEVEL_ATTEMPTS = 50;

    private final PlatformGeneratorService platformGeneratorService;
    private final CoinGeneratorService coinGeneratorService;
    private final LevelValidatorService levelValidatorService;
    private final LevelDefaultsService levelDefaultsService;
    private final EnemySpawnService enemySpawnService;

    public LevelService(
            PlatformGeneratorService platformGeneratorService,
            CoinGeneratorService coinGeneratorService,
            LevelValidatorService levelValidatorService,
            LevelDefaultsService levelDefaultsService,
            EnemySpawnService enemySpawnService
    ) {
        this.platformGeneratorService = platformGeneratorService;
        this.coinGeneratorService = coinGeneratorService;
        this.levelValidatorService = levelValidatorService;
        this.levelDefaultsService = levelDefaultsService;
        this.enemySpawnService = enemySpawnService;
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
            List<EnemySpawnDto> enemies = enemySpawnService.generateEnemies(platforms);

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
}