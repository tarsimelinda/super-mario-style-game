package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinValidator {

    private static final int MIN_COIN_Y = 50;
    private static final int MAX_COIN_Y = 500;

    private static final int FORBIDDEN_AREA_UNDER_PLATFORM = 60;

    private static final int MIN_DISTANCE_ABOVE_PLATFORM = 25;
    private static final int MAX_DISTANCE_ABOVE_PLATFORM = 110;

    public boolean isValidCoinPosition(CoinDto coin, List<PlatformDto> platforms) {
        if (isTooHighOrTooLow(coin)) {
            return false;
        }

        if (overlapsAnyPlatform(coin, platforms)) {
            return false;
        }

        if (isDirectlyUnderAnyPlatform(coin, platforms)) {
            return false;
        }

        return isReachableFromSomePlatform(coin, platforms);
    }

    private boolean isTooHighOrTooLow(CoinDto coin) {
        return coin.y() < MIN_COIN_Y || coin.y() > MAX_COIN_Y;
    }

    private boolean overlapsAnyPlatform(CoinDto coin, List<PlatformDto> platforms) {
        return platforms.stream()
                .anyMatch(platform -> overlaps(coin, platform));
    }

    private boolean overlaps(CoinDto coin, PlatformDto platform) {
        return coin.left() < platform.right()
                && coin.right() > platform.left()
                && coin.top() < platform.bottom()
                && coin.bottom() > platform.top();
    }

    private boolean isDirectlyUnderAnyPlatform(CoinDto coin, List<PlatformDto> platforms) {
        return platforms.stream()
                .anyMatch(platform -> {
                    boolean horizontallyInsidePlatform =
                            coin.centerX() >= platform.left()
                                    && coin.centerX() <= platform.right();

                    boolean belowPlatform =
                            coin.top() >= platform.bottom()
                                    && coin.top() <= platform.bottom() + FORBIDDEN_AREA_UNDER_PLATFORM;

                    return horizontallyInsidePlatform && belowPlatform;
                });
    }

    private boolean isReachableFromSomePlatform(CoinDto coin, List<PlatformDto> platforms) {
        return platforms.stream()
                .anyMatch(platform -> {
                    boolean horizontallyNearPlatform =
                            coin.centerX() >= platform.left() - 40
                                    && coin.centerX() <= platform.right() + 40;

                    boolean abovePlatform =
                            coin.bottom() <= platform.top();

                    int distanceAbovePlatform = platform.top() - coin.bottom();

                    boolean goodHeight =
                            distanceAbovePlatform >= MIN_DISTANCE_ABOVE_PLATFORM
                                    && distanceAbovePlatform <= MAX_DISTANCE_ABOVE_PLATFORM;

                    return horizontallyNearPlatform && abovePlatform && goodHeight;
                });
    }
}