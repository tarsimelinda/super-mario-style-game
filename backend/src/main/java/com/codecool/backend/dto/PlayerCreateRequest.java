package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PlayerCreateRequest(
        @NotBlank String name,
        @Min(0) int hp,
        @Min(0) int coins,
        Boolean shield
) {}
