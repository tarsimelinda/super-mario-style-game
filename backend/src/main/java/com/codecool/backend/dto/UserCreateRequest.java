package com.codecool.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
        @NotBlank(message = "Name is required") String name,
        Integer checkpoint,
        String character
) {}
