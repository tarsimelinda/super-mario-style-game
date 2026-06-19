package com.codecool.backend.service;

import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class PlatformGeneratorService {

    private static final int MIN_PLATFORM_COUNT = 10;
    private static final int MAX_PLATFORM_COUNT = 24;

    private static final int MIN_PLATFORM_WIDTH = 70;
    private static final int MAX_PLATFORM_WIDTH = 240;

    private static final int MIN_PLATFORM_Y = 90;
    private static final int MAX_PLATFORM_Y = 470;

    private static final int MIN_HORIZONTAL_GAP = 45;
    private static final int MIN_VERTICAL_GAP = 35;

    private static final int ZONE_COUNT = 6;
    private static final int MAX_ATTEMPTS = 4000;

    private final PlatformReachabilityService platformReachabilityService;

    private final Random random = new Random();

    public PlatformGeneratorService(PlatformReachabilityService platformReachabilityService) {
        this.platformReachabilityService = platformReachabilityService;
    }

    public List<PlatformDto> generatePlatforms() {
        List<PlatformDto> platforms = new ArrayList<>();

        PlatformDto startPlatform = new PlatformDto(80, 460, 220, GameConstants.PLATFORM_HEIGHT);
        platforms.add(startPlatform);

        int targetPlatformCount = randomBetween(MIN_PLATFORM_COUNT, MAX_PLATFORM_COUNT);

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
        int zone = random.nextInt(ZONE_COUNT);

        return generateCandidateInZone(zone, zoneWidth);
    }

    private PlatformDto generateCandidateInZone(int zone, int zoneWidth) {
        int width = randomBetween(MIN_PLATFORM_WIDTH, MAX_PLATFORM_WIDTH);

        int zoneStartX = zone * zoneWidth;
        int zoneEndX = Math.min(GameConstants.CANVAS_WIDTH, zoneStartX + zoneWidth);

        int minX = zoneStartX + 10;
        int maxX = zoneEndX - width - 10;

        if (maxX < minX) {
            maxX = minX;
        }

        int x = clamp(randomBetween(minX, maxX), 0, GameConstants.CANVAS_WIDTH - width);
        int y = randomBetween(MIN_PLATFORM_Y, MAX_PLATFORM_Y);

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