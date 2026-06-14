package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @Min(value = 1, message = "Checkpoint must be at least 1")
        Integer checkpoint,

        @NotBlank(message = "Character is required")
        @Size(max = 50, message = "Character must be at most 50 characters")
        String character
) {}