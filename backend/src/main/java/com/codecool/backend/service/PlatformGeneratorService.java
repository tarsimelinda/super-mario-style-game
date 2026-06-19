package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PlatformGeneratorService {

    private static final int MIN_PLATFORM_WIDTH = 70;
    private static final int MAX_PLATFORM_WIDTH = 240;

    private static final int MIN_PLATFORM_Y = 90;
    private static final int MAX_PLATFORM_Y = 470;

    private static final int MIN_HORIZONTAL_GAP = 45;
    private static final int MIN_VERTICAL_GAP = 35;

    private static final int ZONE_COUNT = 6;
    private static final int MAX_ATTEMPTS = 4000;

    private final RandomService randomService;
    private final GameBalanceProperties gameBalanceProperties;

    private final PlatformReachabilityService platformReachabilityService;

    public PlatformGeneratorService(
            PlatformReachabilityService platformReachabilityService,
            RandomService randomService,
            GameBalanceProperties gameBalanceProperties
    ) {
        this.platformReachabilityService = platformReachabilityService;
        this.randomService = randomService;
        this.gameBalanceProperties = gameBalanceProperties;
    }

    public List<PlatformDto> generatePlatforms() {
        List<PlatformDto> platforms = new ArrayList<>();

        PlatformDto startPlatform = new PlatformDto(80, 460, 220, GameConstants.PLATFORM_HEIGHT);
        platforms.add(startPlatform);

        int targetPlatformCount = randomService.betweenInclusive(
                gameBalanceProperties.platform().minCount(),
                gameBalanceProperties.platform().maxCount()
        );

        addBasePlatformsAcrossZones(platforms);

        int attempts = 0;

        while (platforms.size() < targetPlatformCount && attempts < MAX_ATTEMPTS) {
            attempts++;

            PlatformDto candidate = generateCandidateInRandomZone();

            if (isValidPlatform(candidate, platforms)) {
                platforms.add(candidate);
            }
        }

        return platforms.stream()
                .sorted(Comparator.comparingInt(PlatformDto::x))
                .toList();
    }

    private void addBasePlatformsAcrossZones(List<PlatformDto> platforms) {
        int zoneWidth = GameConstants.CANVAS_WIDTH / ZONE_COUNT;

        for (int zone = 0; zone < ZONE_COUNT; zone++) {
            boolean startZoneAlreadyHasPlatform = zone == 0;

            if (startZoneAlreadyHasPlatform) {
                continue;
            }

            for (int attempt = 0; attempt < 100; attempt++) {
                PlatformDto candidate = generateCandidateInZone(zone, zoneWidth);

                if (isValidPlatform(candidate, platforms)) {
                    platforms.add(candidate);
                    break;
                }
            }
        }
    }

    private PlatformDto generateCandidateInRandomZone() {
        int zoneWidth = GameConstants.CANVAS_WIDTH / ZONE_COUNT;
        int zone = randomService.betweenInclusive(0, ZONE_COUNT - 1);

        return generateCandidateInZone(zone, zoneWidth);
    }

    private PlatformDto generateCandidateInZone(int zone, int zoneWidth) {
        int width = randomService.betweenInclusive(MIN_PLATFORM_WIDTH, MAX_PLATFORM_WIDTH);

        int zoneStartX = zone * zoneWidth;
        int zoneEndX = Math.min(GameConstants.CANVAS_WIDTH, zoneStartX + zoneWidth);

        int minX = zoneStartX + 10;
        int maxX = zoneEndX - width - 10;

        if (maxX < minX) {
            maxX = minX;
        }

        int x = clamp(randomService.betweenInclusive(minX, maxX), 0, GameConstants.CANVAS_WIDTH - width);
        int y = randomService.betweenInclusive(MIN_PLATFORM_Y, MAX_PLATFORM_Y);

        return new PlatformDto(x, y, width, GameConstants.PLATFORM_HEIGHT);
    }

    private boolean isValidPlatform(PlatformDto candidate, List<PlatformDto> existingPlatforms) {
        if (candidate.bottom() >= GameConstants.GROUND_Y) {
            return false;
        }

        if (isTooCloseToAnyPlatform(candidate, existingPlatforms)) {
            return false;
        }

        return isReachableOrDroppable(candidate, existingPlatforms);
    }

    private boolean isTooCloseToAnyPlatform(PlatformDto candidate, List<PlatformDto> existingPlatforms) {
        return existingPlatforms.stream()
                .anyMatch(existing -> areTooClose(candidate, existing));
    }

    private boolean areTooClose(PlatformDto a, PlatformDto b) {
        boolean horizontallyOverlappingOrTooClose =
                a.left() < b.right() + MIN_HORIZONTAL_GAP
                        && a.right() > b.left() - MIN_HORIZONTAL_GAP;

        boolean verticallyOverlappingOrTooClose =
                a.top() < b.bottom() + MIN_VERTICAL_GAP
                        && a.bottom() > b.top() - MIN_VERTICAL_GAP;

        return horizontallyOverlappingOrTooClose && verticallyOverlappingOrTooClose;
    }

    private boolean isReachableOrDroppable(PlatformDto candidate, List<PlatformDto> existingPlatforms) {
        return existingPlatforms.stream()
                .anyMatch(existing -> platformReachabilityService.canMoveFromTo(existing, candidate));
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

}