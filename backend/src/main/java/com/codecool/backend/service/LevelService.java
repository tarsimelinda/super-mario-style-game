package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {

    private final PlatformGeneratorService platformGeneratorService;
    private final CoinGeneratorService coinGeneratorService;
    private final LevelValidatorService levelValidatorService;
    private final LevelDefaultsService levelDefaultsService;
    private final EnemySpawnService enemySpawnService;
    private final GameBalanceProperties gameBalanceProperties;

    public LevelService(
            PlatformGeneratorService platformGeneratorService,
            CoinGeneratorService coinGeneratorService,
            LevelValidatorService levelValidatorService,
            LevelDefaultsService levelDefaultsService,
            EnemySpawnService enemySpawnService,
            GameBalanceProperties gameBalanceProperties
    ) {
        this.platformGeneratorService = platformGeneratorService;
        this.coinGeneratorService = coinGeneratorService;
        this.levelValidatorService = levelValidatorService;
        this.levelDefaultsService = levelDefaultsService;
        this.enemySpawnService = enemySpawnService;
        this.gameBalanceProperties = gameBalanceProperties;
    }

    public LevelDto getDefaultLevel() {
        return levelDefaultsService.getDefaultLevel();
    }

    public LevelDto getRandomLevel() {
        for (int attempt = 0; attempt < gameBalanceProperties.level().randomLevelAttempts(); attempt++) {
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
                    GameConstants.DEFAULT_LIVES,
                    platforms,
                    enemies,
                    coins
            );
        }

        return getDefaultLevel();
    }
}