package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record PlayerPatchRequest(
        @Min(value = 0, message = "HP cannot be negative")
        Integer hp,

        @Min(value = 0, message = "Coins cannot be negative")
        Integer coins,

        @Pattern(
                regexp = "playing|menu|dead",
                message = "Status must be one of: playing, menu, dead"
        )
        String status
) {}