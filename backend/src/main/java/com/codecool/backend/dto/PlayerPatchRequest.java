package com.codecool.backend.dto;

public record PlayerPatchRequest(
        Integer hp,
        Integer coins,
        Boolean shield,
        String status
) {}
