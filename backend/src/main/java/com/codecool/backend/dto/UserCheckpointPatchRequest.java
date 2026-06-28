package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserCheckpointPatchRequest(
        @NotNull(message = "Checkpoint is required")
        @Min(value = 1, message = "Checkpoint must be at least 1")
        Integer checkpoint
) {
}