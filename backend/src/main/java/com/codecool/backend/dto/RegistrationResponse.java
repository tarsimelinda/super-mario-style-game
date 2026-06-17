package com.codecool.backend.dto;

import com.codecool.backend.model.PlayerStatus;

public record RegistrationResponse(
        String userId,
        String playerId,
        String name,
        String character,
        String characterColor,
        int checkpoint,
        int hp,
        int coins,
        PlayerStatus status
) {
}