package com.codecool.backend.dto;

public record CharacterOptionResponse(
        String id,
        String key,
        String displayName,
        String color,
        String imageUrl
) {
}