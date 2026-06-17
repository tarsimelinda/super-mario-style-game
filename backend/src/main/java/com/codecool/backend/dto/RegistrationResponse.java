package com.codecool.backend.dto;

public record RegistrationResponse(
        String userId,
        String playerId,
        String name,
        String character,
        String characterColor,
        int checkpoint,
        int hp,
        int coins,
        String status
) {
}