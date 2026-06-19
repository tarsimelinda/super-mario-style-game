package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CoinGeneratorService {

    private static final int MIN_HEIGHT_ABOVE_PLATFORM = 35;
    private static final int MAX_HEIGHT_ABOVE_PLATFORM = 80;

    private static final int PLATFORM_SIDE_MARGIN = 20;

    private static final int MAX_COINS_PER_PLATFORM = 3;

    private final RandomService randomService;
    private final CoinValidator coinValidator;
    private final GameBalanceProperties gameBalanceProperties;

    public CoinGeneratorService(
            CoinValidator coinValidator,
            RandomService randomService,
            GameBalanceProperties gameBalanceProperties
    ) {
        this.coinValidator = coinValidator;
        this.randomService = randomService;
        this.gameBalanceProperties = gameBalanceProperties;
    }

    public List<CoinDto> generateCoins(List<PlatformDto> platforms) {
        List<CoinDto> coins = new ArrayList<>();

        int targetCoinCount = randomService.betweenInclusive(
                gameBalanceProperties.coin().minCount(),
                gameBalanceProperties.coin().maxCount()
        );

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
            return randomService.betweenInclusive(0, 1);
        }

        if (platform.width() < 170) {
            return randomService.betweenInclusive(1, 2);
        }

        return randomService.betweenInclusive(1, MAX_COINS_PER_PLATFORM);
    }

    private CoinDto generateCoinAbovePlatform(
            PlatformDto platform,
            int coinIndex,
            int coinsOnThisPlatform
    ) {
        int usableLeft = platform.left() + PLATFORM_SIDE_MARGIN;
        int usableRight = platform.right() - PLATFORM_SIDE_MARGIN - GameConstants.COIN_WIDTH;

        int x;

        if (coinsOnThisPlatform <= 1) {
            x = platform.centerX() - GameConstants.COIN_WIDTH / 2;
        } else {
            int usableWidth = usableRight - usableLeft;
            int spacing = usableWidth / Math.max(1, coinsOnThisPlatform - 1);
            x = usableLeft + spacing * coinIndex;
            x += randomService.betweenInclusive(-8, 8);
        }

        x = clamp(x, usableLeft, usableRight);

        int distanceAbovePlatform = randomService.betweenInclusive(
                MIN_HEIGHT_ABOVE_PLATFORM,
                MAX_HEIGHT_ABOVE_PLATFORM
        );

        int y = platform.top() - distanceAbovePlatform - GameConstants.COIN_HEIGHT;

        return new CoinDto(x, y, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT);
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

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}