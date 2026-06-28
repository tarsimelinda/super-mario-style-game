package com.codecool.backend.service;

import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.dto.PointDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelDefaultsServiceTest {

    private LevelDefaultsService service;

    @BeforeEach
    void setUp() {
        service = new LevelDefaultsService();
    }

    @Test
    void getDefaultLevelShouldReturnPlayerStartPosition() {
        LevelDto level = service.getDefaultLevel();

        assertEquals(
                new PointDto(150, GameConstants.GROUND_Y - GameConstants.PLAYER_HEIGHT),
                level.playerStart()
        );
    }

    @Test
    void getDefaultLevelShouldReturnDefaultLives() {
        LevelDto level = service.getDefaultLevel();

        assertEquals(GameConstants.DEFAULT_LIVES, level.lives());
    }

    @Test
    void getDefaultLevelShouldReturnDefaultPlatforms() {
        LevelDto level = service.getDefaultLevel();

        assertEquals(8, level.platforms().size());

        assertEquals(
                new PlatformDto(100, 450, 200, GameConstants.PLATFORM_HEIGHT),
                level.platforms().get(0)
        );

        assertEquals(
                new PlatformDto(100, 150, 70, GameConstants.PLATFORM_HEIGHT),
                level.platforms().get(level.platforms().size() - 1)
        );
    }

    @Test
    void getDefaultLevelShouldReturnDefaultEnemies() {
        LevelDto level = service.getDefaultLevel();

        assertEquals(2, level.enemies().size());

        assertEquals(
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
                level.enemies().get(0)
        );

        assertEquals(
                new EnemySpawnDto(
                        300,
                        160,
                        GameConstants.ENEMY_WIDTH,
                        GameConstants.ENEMY_HEIGHT,
                        3,
                        2,
                        1,
                        "black",
                        false
                ),
                level.enemies().get(1)
        );
    }

    @Test
    void getDefaultLevelShouldReturnDefaultCoins() {
        LevelDto level = service.getDefaultLevel();

        assertEquals(10, level.coins().size());

        assertEquals(
                new CoinDto(150, 410, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),
                level.coins().get(0)
        );

        assertEquals(
                new CoinDto(130, 110, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT),
                level.coins().get(level.coins().size() - 1)
        );
    }

    @Test
    void getDefaultLevelShouldNotReturnNullCollections() {
        LevelDto level = service.getDefaultLevel();

        assertNotNull(level.platforms());
        assertNotNull(level.enemies());
        assertNotNull(level.coins());
    }

    @Test
    void randomLevelStartPlatformShouldUseExpectedCoordinates() {
        PlatformDto platform = LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM;

        assertEquals(
                new PlatformDto(80, 460, 220, GameConstants.PLATFORM_HEIGHT),
                platform
        );
    }

    @Test
    void randomLevelPlayerStartShouldUseExpectedCoordinates() {
        PointDto point = LevelDefaultsService.RANDOM_LEVEL_PLAYER_START;

        assertEquals(
                new PointDto(120, GameConstants.GROUND_Y - GameConstants.PLAYER_HEIGHT),
                point
        );
    }
}