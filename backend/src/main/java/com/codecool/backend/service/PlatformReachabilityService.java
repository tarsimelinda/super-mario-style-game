package com.codecool.backend.service;

import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

@Service
public class PlatformReachabilityService {

    private static final int MAX_JUMP_UP = 90;
    private static final int MAX_DROP_DOWN = 260;

    private static final int MAX_HORIZONTAL_JUMP_REACH = 170;
    private static final int MAX_HORIZONTAL_DROP_REACH = 220;

    private static final int FALL_OVERLAP_MARGIN = 50;

    public boolean canMoveFromTo(PlatformDto from, PlatformDto to) {
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