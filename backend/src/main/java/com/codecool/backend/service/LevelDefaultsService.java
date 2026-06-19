package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.dto.PointDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelDefaultsService {

    public static final int DEFAULT_LIVES = 3;

    private static final int GROUND_Y = 550;
    private static final int PLAYER_HEIGHT = 50;

    public static final PlatformDto RANDOM_LEVEL_START_PLATFORM =
            new PlatformDto(80, 460, 220, 20);

    public static final PointDto RANDOM_LEVEL_PLAYER_START =
            new PointDto(120, GROUND_Y - PLAYER_HEIGHT);

    private static final PointDto DEFAULT_LEVEL_PLAYER_START =
            new PointDto(150, GROUND_Y - PLAYER_HEIGHT);

    public LevelDto getDefaultLevel() {
        List<PlatformDto> platforms = getDefaultPlatforms();

        return new LevelDto(
                DEFAULT_LEVEL_PLAYER_START,
                DEFAULT_LIVES,
                platforms,
                getDefaultEnemies(),
                getDefaultCoins()
        );
    }

    private List<PlatformDto> getDefaultPlatforms() {
        return List.of(
                new PlatformDto(100, 450, 200, 20),
                new PlatformDto(400, 380, 150, 20),
                new PlatformDto(650, 320, 200, 20),
                new PlatformDto(950, 250, 150, 20),
                new PlatformDto(1250, 200, 50, 20),
                new PlatformDto(650, 150, 200, 20),
                new PlatformDto(400, 100, 100, 20),
                new PlatformDto(100, 150, 70, 20)
        );
    }

    private List<EnemySpawnDto> getDefaultEnemies() {
        return List.of(
                new EnemySpawnDto(500, 310, 40, 40, 2, 1, 1, "blue", false),
                new EnemySpawnDto(300, 160, 40, 40, 3, 2, 1, "black", false)
        );
    }

    private List<CoinDto> getDefaultCoins() {
        return List.of(
                new CoinDto(150, 410, 20, 20),
                new CoinDto(230, 410, 20, 20),

                new CoinDto(450, 340, 20, 20),
                new CoinDto(510, 340, 20, 20),

                new CoinDto(710, 280, 20, 20),
                new CoinDto(790, 280, 20, 20),

                new CoinDto(1000, 210, 20, 20),

                new CoinDto(700, 110, 20, 20),
                new CoinDto(760, 110, 20, 20),

                new CoinDto(130, 110, 20, 20)
        );
    }
}