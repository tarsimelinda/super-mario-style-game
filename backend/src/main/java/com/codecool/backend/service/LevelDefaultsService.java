package com.codecool.backend.service;

import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.dto.PointDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelDefaultsService {

    public static final PlatformDto RANDOM_LEVEL_START_PLATFORM =
            new PlatformDto(80, 460, 220, GameConstants.PLATFORM_HEIGHT);

    public static final PointDto RANDOM_LEVEL_PLAYER_START =
            new PointDto(120, GameConstants.GROUND_Y - GameConstants.PLAYER_HEIGHT);

    private static final PointDto DEFAULT_LEVEL_PLAYER_START =
            new PointDto(150, GameConstants.GROUND_Y - GameConstants.PLAYER_HEIGHT);

    public LevelDto getDefaultLevel() {
        List<PlatformDto> platforms = getDefaultPlatforms();

        return new LevelDto(
                DEFAULT_LEVEL_PLAYER_START,
                GameConstants.DEFAULT_LIVES,
                platforms,
                getDefaultEnemies(),
                getDefaultCoins()
        );
    }

    private List<PlatformDto> getDefaultPlatforms() {
        return List.of(
                new PlatformDto(100, 450, 200, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(400, 380, 150, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(650, 320, 200, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(950, 250, 150, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(1250, 200, 50, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(650, 150, 200, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(400, 100, 100, GameConstants.PLATFORM_HEIGHT),
                new PlatformDto(100, 150, 70, GameConstants.PLATFORM_HEIGHT)
        );
    }

    private List<EnemySpawnDto> getDefaultEnemies() {
        return List.of(
                new EnemySpawnDto(
                        500,
                        310,
                        GameConstants.ENEMY_WIDTH,
                        GameConstants.ENEMY_HEIGHT,
                        2,
                        1,
                        1,
                        "blue",
                        false
                ),
                new EnemySpawnDto(
                        300,
                        160,
                        GameConstants.ENEMY_WIDTH,
                        GameConstants.ENEMY_HEIGHT,
                        3,
                        2,
                        1,
                        "black",
                        false)
        );
    }

    private List<CoinDto> getDefaultCoins() {
        return List.of(
                new CoinDto(150, 410, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),
                new CoinDto(230, 410, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),

                new CoinDto(450, 340, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),
                new CoinDto(510, 340, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),

                new CoinDto(710, 280, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),
                new CoinDto(790, 280, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),

                new CoinDto(1000, 210, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),

                new CoinDto(700, 110, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),
                new CoinDto(760, 110, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),

                new CoinDto(130, 110, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT)
        );
    }
}