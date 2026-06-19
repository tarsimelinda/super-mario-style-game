package com.codecool.backend.dto;

import com.codecool.backend.model.PlayerStatus;

public record PlayerResponse(
        String id,
        String userId,
        int hp,
        int coins,
        PlayerStatus status
) {
}