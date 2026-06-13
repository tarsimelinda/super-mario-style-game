package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class CoinGeneratorService {

    private static final int COIN_WIDTH = 20;
    private static final int COIN_HEIGHT = 20;

    private static final int MIN_COIN_COUNT = 10;
    private static final int MAX_COIN_COUNT = 25;

    private static final int MIN_HEIGHT_ABOVE_PLATFORM = 35;
    private static final int MAX_HEIGHT_ABOVE_PLATFORM = 80;

    private static final int PLATFORM_SIDE_MARGIN = 20;

    private static final int MAX_COINS_PER_PLATFORM = 3;

    private final Random random = new Random();
    private final CoinValidator coinValidator;

    public CoinGeneratorService(CoinValidator coinValidator) {
        this.coinValidator = coinValidator;
    }

    public List<CoinDto> generateCoins(List<PlatformDto> platforms) {
        List<CoinDto> coins = new ArrayList<>();

        int targetCoinCount = randomBetween(MIN_COIN_COUNT, MAX_COIN_COUNT);

        List<PlatformDto> shuffledPlatforms = new ArrayList<>(platforms);
        java.util.Collections.shuffle(shuffledPlatforms);

        for (PlatformDto platform : shuffledPlatforms) {
            if (coins.size() >= targetCoinCount) {
                break;
            }

            int coinsForPlatform = calculateCoinCountForPlatform(platform);

            for (int i = 0; i < coinsForPlatform; i++) {
                if (coins.size() >= targetCoinCount) {
                    break;
                }

                CoinDto candidate = generateCoinAbovePlatform(platform, i, coinsForPlatform);

                if (
                        coinValidator.isValidCoinPosition(candidate, platforms)
                                && !overlapsAnyExistingCoin(candidate, coins)
                ) {
                    coins.add(candidate);
                }
            }
        }

        return coins.stream()
                .sorted(Comparator.comparingInt(CoinDto::x))
                .toList();
    }

    private int calculateCoinCountForPlatform(PlatformDto platform) {
        if (platform.width() < 100) {
            return randomBetween(0, 1);
        }

        if (platform.width() < 170) {
            return randomBetween(1, 2);
        }

        return randomBetween(1, MAX_COINS_PER_PLATFORM);
    }

    private CoinDto generateCoinAbovePlatform(
            PlatformDto platform,
            int coinIndex,
            int coinsOnThisPlatform
    ) {
        int usableLeft = platform.left() + PLATFORM_SIDE_MARGIN;
        int usableRight = platform.right() - PLATFORM_SIDE_MARGIN - COIN_WIDTH;

        int x;

        if (coinsOnThisPlatform <= 1) {
            x = platform.centerX() - COIN_WIDTH / 2;
        } else {
            int usableWidth = usableRight - usableLeft;
            int spacing = usableWidth / Math.max(1, coinsOnThisPlatform - 1);
            x = usableLeft + spacing * coinIndex;
            x += randomBetween(-8, 8);
        }

        x = clamp(x, usableLeft, usableRight);

        int distanceAbovePlatform = randomBetween(
                MIN_HEIGHT_ABOVE_PLATFORM,
                MAX_HEIGHT_ABOVE_PLATFORM
        );

        int y = platform.top() - distanceAbovePlatform - COIN_HEIGHT;

        return new CoinDto(x, y, COIN_WIDTH, COIN_HEIGHT);
    }

    private boolean overlapsAnyExistingCoin(CoinDto candidate, List<CoinDto> existingCoins) {
        return existingCoins.stream()
                .anyMatch(existing -> overlaps(candidate, existing));
    }

    private boolean overlaps(CoinDto a, CoinDto b) {
        return a.left() < b.right()
                && a.right() > b.left()
                && a.top() < b.bottom()
                && a.bottom() > b.top();
    }

    private int randomBetween(int min, int max) {
        if (max < min) {
            return min;
        }

        return random.nextInt(max - min + 1) + min;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}