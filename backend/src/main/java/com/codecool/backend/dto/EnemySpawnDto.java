package com.codecool.backend.dto;

public record EnemySpawnDto(
        int x,
        int y,
        int width,
        int height,
        int speed,
        int damage,
        int hp,
        String color,
        boolean canJump
) {
}
