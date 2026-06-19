package com.codecool.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.game.balance")
public record GameBalanceProperties(
        Level level,
        Platform platform,
        Coin coin,
        Enemy enemy
) {
    public record Level(
            int randomLevelAttempts
    ) {
    }

    public record Platform(
            int minCount,
            int maxCount
    ) {
    }

    public record Coin(
            int minCount,
            int maxCount
    ) {
    }

    public record Enemy(
            int spawnChancePercent,
            int minCount,
            int maxCount
    ) {
    }
}