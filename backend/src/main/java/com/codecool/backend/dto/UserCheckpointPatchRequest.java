package com.codecool.backend.dto;

import jakarta.validation.constraints.Min;

public record UserCheckpointPatchRequest(
        @Min(value = 1, message = "Checkpoint must be at least 1")
        int checkpoint
) {
}