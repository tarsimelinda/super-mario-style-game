package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.dto.PointDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {

    public LevelDto getDefaultLevel() {
        return new LevelDto(
                new PointDto(50, 200),
                3,
                getPlatforms(),
                getEnemies(),
                getCoins()
        );
    }

    private List<PlatformDto> getPlatforms() {
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

    private List<EnemySpawnDto> getEnemies() {
        return List.of(
                new EnemySpawnDto(500, 310, 40, 40, 2),
                new EnemySpawnDto(300, 160, 40, 40, 2)
        );
    }

    private List<CoinDto> getCoins() {
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