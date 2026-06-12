package com.codecool.backend.dto;

import java.util.List;

public record LevelDto(
        PointDto playerStart,
        int lives,
        List<PlatformDto> platforms,
        List<EnemySpawnDto> enemies,
        List<CoinDto> coins
) {
}
