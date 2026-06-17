package com.codecool.backend.dto;

public record PlayerPatchRequest(
        Integer hp,
        Integer coins,
        String status
) {}