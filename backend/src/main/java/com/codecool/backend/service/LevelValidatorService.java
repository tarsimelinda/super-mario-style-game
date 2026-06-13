package com.codecool.backend.service;

import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LevelValidatorService {

    public boolean isPlayable(List<PlatformDto> platforms) {
        if (platforms.isEmpty()) {
            return false;
        }

        PlatformDto startPlatform = platforms.get(0);
        Set<PlatformDto> reachable = findReachablePlatforms(startPlatform, platforms);

        return reachable.size() == platforms.size();
    }

    private Set<PlatformDto> findReachablePlatforms(
            PlatformDto startPlatform,
            List<PlatformDto> platforms
    ) {
        Set<PlatformDto> visited = new HashSet<>();
        Queue<PlatformDto> queue = new LinkedList<>();

        visited.add(startPlatform);
        queue.add(startPlatform);

        while (!queue.isEmpty()) {
            PlatformDto current = queue.poll();

            for (PlatformDto next : platforms) {
                if (!visited.contains(next) && canMoveFromTo(current, next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return visited;
    }

    private boolean canMoveFromTo(PlatformDto from, PlatformDto to) {
        int verticalDifference = to.y() - from.y();

        if (verticalDifference < 0) {
            return canJumpUpToPlatform(from, to, Math.abs(verticalDifference));
        }

        return canDropDownToPlatform(from, to, verticalDifference);
    }

    private boolean canJumpUpToPlatform(PlatformDto from, PlatformDto to, int heightDifference) {
        int horizontalGap = horizontalGapBetween(from, to);

        return heightDifference <= 100
                && horizontalGap <= 190;
    }

    private boolean canDropDownToPlatform(PlatformDto from, PlatformDto to, int dropDistance) {
        int horizontalGap = horizontalGapBetween(from, to);

        boolean horizontallyClose = horizontalGap <= 170;

        boolean overlapsEnough =
                from.right() > to.left() - 40
                        && from.left() < to.right() + 40;

        return dropDistance <= 240
                && (horizontallyClose || overlapsEnough);
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