package com.codecool.backend.service;

import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.PlatformDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatformReachabilityServiceTest {

    private PlatformReachabilityService service;

    @BeforeEach
    void setUp() {
        service = new PlatformReachabilityService();
    }

    @Test
    void canMoveFromToShouldReturnFalseWhenPlatformsAreTheSame() {
        PlatformDto platform = platform(100, 400, 150);

        boolean result = service.canMoveFromTo(platform, platform);

        assertFalse(result);
    }

    @Test
    void canMoveFromToShouldReturnTrueWhenJumpingUpWithinVerticalAndHorizontalReach() {
        PlatformDto from = platform(100, 400, 150);
        PlatformDto to = platform(320, 320, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertTrue(result);
    }

    @Test
    void canMoveFromToShouldReturnFalseWhenJumpingUpTooHigh() {
        PlatformDto from = platform(100, 400, 150);
        PlatformDto to = platform(320, 309, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertFalse(result);
    }

    @Test
    void canMoveFromToShouldReturnFalseWhenJumpingUpTooFarHorizontally() {
        PlatformDto from = platform(100, 400, 150);
        PlatformDto to = platform(421, 320, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertFalse(result);
    }

    @Test
    void canMoveFromToShouldReturnTrueWhenDroppingDownWithinHorizontalReach() {
        PlatformDto from = platform(100, 250, 150);
        PlatformDto to = platform(470, 450, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertTrue(result);
    }

    @Test
    void canMoveFromToShouldReturnFalseWhenDroppingDownTooFarVertically() {
        PlatformDto from = platform(100, 100, 150);
        PlatformDto to = platform(100, 361, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertFalse(result);
    }

    @Test
    void canMoveFromToShouldReturnFalseWhenDroppingDownTooFarHorizontallyWithoutFallOverlap() {
        PlatformDto from = platform(100, 250, 150);
        PlatformDto to = platform(471, 450, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertFalse(result);
    }

    @Test
    void canMoveFromToShouldReturnTrueWhenDroppingDownWithFallOverlapMargin() {
        PlatformDto from = platform(100, 250, 150);
        PlatformDto to = platform(295, 450, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertTrue(result);
    }

    @Test
    void canMoveFromToShouldReturnTrueWhenPlatformsOverlapHorizontallyAndTargetIsAboveWithinJumpHeight() {
        PlatformDto from = platform(100, 400, 150);
        PlatformDto to = platform(120, 310, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertTrue(result);
    }

    @Test
    void canMoveFromToShouldReturnTrueWhenPlatformsOverlapHorizontallyAndTargetIsBelowWithinDropDistance() {
        PlatformDto from = platform(100, 200, 150);
        PlatformDto to = platform(120, 460, 150);

        boolean result = service.canMoveFromTo(from, to);

        assertTrue(result);
    }

    private PlatformDto platform(int x, int y, int width) {
        return new PlatformDto(x, y, width, GameConstants.PLATFORM_HEIGHT);
    }
}