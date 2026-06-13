package com.codecool.backend.service;

import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class PlatformGeneratorService {

    private static final int CANVAS_WIDTH = 1350;
    private static final int GROUND_Y = 550;

    private static final int MIN_PLATFORM_COUNT = 10;
    private static final int MAX_PLATFORM_COUNT = 24;

    private static final int PLATFORM_HEIGHT = 20;

    private static final int MIN_PLATFORM_WIDTH = 70;
    private static final int MAX_PLATFORM_WIDTH = 240;

    private static final int MIN_PLATFORM_Y = 90;
    private static final int MAX_PLATFORM_Y = 470;

    private static final int MIN_HORIZONTAL_GAP = 45;
    private static final int MIN_VERTICAL_GAP = 35;

    private static final int MAX_JUMP_UP = 90;
    private static final int MAX_DROP_DOWN = 260;

    private static final int MAX_HORIZONTAL_JUMP_REACH = 170;
    private static final int MAX_HORIZONTAL_DROP_REACH = 220;
    private static final int FALL_OVERLAP_MARGIN = 50;

    private static final int MAX_HORIZONTAL_REACH = 230;

    private static final int ZONE_COUNT = 6;
    private static final int MAX_ATTEMPTS = 4000;

    private final Random random = new Random();

    public List<PlatformDto> generatePlatforms() {
        List<PlatformDto> platforms = new ArrayList<>();

        PlatformDto startPlatform = new PlatformDto(80, 460, 220, PLATFORM_HEIGHT);
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
        int zoneWidth = CANVAS_WIDTH / ZONE_COUNT;

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
        int zoneWidth = CANVAS_WIDTH / ZONE_COUNT;
        int zone = random.nextInt(ZONE_COUNT);

        return generateCandidateInZone(zone, zoneWidth);
    }

    private PlatformDto generateCandidateInZone(int zone, int zoneWidth) {
        int width = randomBetween(MIN_PLATFORM_WIDTH, MAX_PLATFORM_WIDTH);

        int zoneStartX = zone * zoneWidth;
        int zoneEndX = Math.min(CANVAS_WIDTH, zoneStartX + zoneWidth);

        int minX = zoneStartX + 10;
        int maxX = zoneEndX - width - 10;

        if (maxX < minX) {
            maxX = minX;
        }

        int x = clamp(randomBetween(minX, maxX), 0, CANVAS_WIDTH - width);
        int y = randomBetween(MIN_PLATFORM_Y, MAX_PLATFORM_Y);

        return new PlatformDto(x, y, width, PLATFORM_HEIGHT);
    }

    private boolean isValidPlatform(PlatformDto candidate, List<PlatformDto> existingPlatforms) {
        if (candidate.bottom() >= GROUND_Y) {
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
                .anyMatch(existing -> canMoveFromTo(existing, candidate));
    }

    private boolean canJumpOrFallBetween(PlatformDto from, PlatformDto to) {
        int horizontalDistance = Math.abs(from.centerX() - to.centerX());
        int verticalDifference = to.y() - from.y();

        boolean horizontallyReachable = horizontalDistance <= MAX_HORIZONTAL_REACH;

        boolean canJumpUp =
                verticalDifference < 0
                        && Math.abs(verticalDifference) <= MAX_JUMP_UP;

        boolean canDropDown =
                verticalDifference >= 0
                        && verticalDifference <= MAX_DROP_DOWN;

        boolean roughlyOverlappingForFall =
                from.right() > to.left() - 80
                        && from.left() < to.right() + 80;

        return horizontallyReachable && (canJumpUp || canDropDown || roughlyOverlappingForFall);
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

    private boolean canMoveFromTo(PlatformDto from, PlatformDto to) {
        if (from.equals(to)) {
            return false;
        }

        int verticalDifference = to.y() - from.y();

        if (verticalDifference < 0) {
            return canJumpUpToPlatform(from, to, Math.abs(verticalDifference));
        }

        return canDropDownToPlatform(from, to, verticalDifference);
    }

    private boolean canJumpUpToPlatform(PlatformDto from, PlatformDto to, int heightDifference) {
        int horizontalGap = horizontalGapBetween(from, to);

        return heightDifference <= MAX_JUMP_UP
                && horizontalGap <= MAX_HORIZONTAL_JUMP_REACH;
    }

    private boolean canDropDownToPlatform(PlatformDto from, PlatformDto to, int dropDistance) {
        int horizontalGap = horizontalGapBetween(from, to);

        boolean horizontallyClose = horizontalGap <= MAX_HORIZONTAL_DROP_REACH;

        boolean canFallOntoIt =
                from.right() > to.left() - FALL_OVERLAP_MARGIN
                        && from.left() < to.right() + FALL_OVERLAP_MARGIN;

        return dropDistance <= MAX_DROP_DOWN
                && (horizontallyClose || canFallOntoIt);
    }

    private int horizontalGapBetween(PlatformDto a, PlatformDto b) {
        if (a.right() < b.left()) {
            return b.left() - a.right();
        }

        if (b.right() < a.left()) {
            return a.left() - b.right();
        }

        return 0;
    }
}