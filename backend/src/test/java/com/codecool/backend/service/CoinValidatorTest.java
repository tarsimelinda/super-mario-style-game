package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.PlatformDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoinValidatorTest {

    private final CoinValidator validator = new CoinValidator();

    @Test
    void isValidCoinPositionShouldReturnTrueWhenCoinIsReachableAndDoesNotOverlapPlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);
        CoinDto coin = createCoin(180, 220, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertTrue(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinIsTooHigh() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);
        CoinDto coin = createCoin(180, 49, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinIsTooLow() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);
        CoinDto coin = createCoin(180, 501, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinOverlapsPlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);
        CoinDto coin = createCoin(150, 310, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinIsDirectlyUnderPlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(180, 350, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinIsNotReachableFromAnyPlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(500, 220, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinIsTooCloseAbovePlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(180, 280, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenCoinIsTooFarAbovePlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(180, 160, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertFalse(result);
    }

    @Test
    void isValidCoinPositionShouldReturnTrueWhenCoinIsExactlyAtMinimumAllowedDistanceAbovePlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(180, 255, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertTrue(result);
    }

    @Test
    void isValidCoinPositionShouldReturnTrueWhenCoinIsExactlyAtMaximumAllowedDistanceAbovePlatform() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(180, 170, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertTrue(result);
    }

    @Test
    void isValidCoinPositionShouldReturnTrueWhenCoinIsHorizontallyNearPlatformLeftEdge() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(60, 220, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertTrue(result);
    }

    @Test
    void isValidCoinPositionShouldReturnTrueWhenCoinIsHorizontallyNearPlatformRightEdge() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        CoinDto coin = createCoin(330, 220, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of(platform));

        assertTrue(result);
    }

    @Test
    void isValidCoinPositionShouldReturnFalseWhenThereAreNoPlatforms() {
        CoinDto coin = createCoin(180, 220, 20, 20);

        boolean result = validator.isValidCoinPosition(coin, List.of());

        assertFalse(result);
    }

    private CoinDto createCoin(int x, int y, int width, int height) {
        return new CoinDto(x, y, width, height);
    }

    private PlatformDto createPlatform(int x, int y, int width, int height) {
        return new PlatformDto(x, y, width, height);
    }
}