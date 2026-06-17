package com.codecool.backend.dto;

import com.codecool.backend.model.PlayerStatus;
import jakarta.validation.constraints.Min;

public record PlayerPatchRequest(
        @Min(value = 0, message = "HP cannot be negative")
        Integer hp,

        @Min(value = 0, message = "Coins cannot be negative")
        Integer coins,

        PlayerStatus status
) {}