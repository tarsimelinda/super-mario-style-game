package com.codecool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CharacterCreateRequest(
        @NotBlank(message = "Character key is required")
        @Size(max = 50, message = "Character key must be at most 50 characters")
        String key,

        @NotBlank(message = "Display name is required")
        @Size(max = 50, message = "Display name must be at most 50 characters")
        String displayName,

        @NotBlank(message = "Color is required")
        @Size(max = 50, message = "Color must be at most 50 characters")
        String color
) {
}