package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EnemyCreateRequest(
        @Min(1) int damage,
        @Min(1) int speed,
        @Min(1) int hp,
        @NotBlank String color,
        Boolean canJump
) {}