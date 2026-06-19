package com.codecool.backend.dto;

public record UserResponse(
        String id,
        String name,
        int checkpoint,
        String character
) {
}