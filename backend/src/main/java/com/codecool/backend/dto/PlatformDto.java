package com.codecool.backend.dto;

public record PlatformDto(
        int x,
        int y,
        int width,
        int height
) {
    public int left() {
        return x;
    }

    public int right() {
        return x + width;
    }

    public int top() {
        return y;
    }

    public int bottom() {
        return y + height;
    }

    public int centerX() {
        return x + width / 2;
    }
}