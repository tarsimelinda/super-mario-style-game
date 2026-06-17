package com.codecool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Character is required")
        @Size(max = 50, message = "Character must be at most 50 characters")
        String character
) {
}