package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EnemyCreateRequest(
        @Min(0) int damage,
        @Min(0) int speed,
        @Min(0) int hp,
        @NotNull String color,
        Boolean canJump
) {}
