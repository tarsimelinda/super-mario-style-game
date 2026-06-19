package com.codecool.backend.dto;

public record EnemyResponse(
        String id,
        int damage,
        int speed,
        int hp,
        String color,
        boolean canJump
) {
}