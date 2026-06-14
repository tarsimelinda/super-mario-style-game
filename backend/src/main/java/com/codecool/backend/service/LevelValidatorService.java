package com.codecool.backend.service;

import com.codecool.backend.dto.PlatformDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LevelValidatorService {

    private final PlatformReachabilityService platformReachabilityService;

    public LevelValidatorService(PlatformReachabilityService platformReachabilityService) {
        this.platformReachabilityService = platformReachabilityService;
    }

    public boolean isPlayable(PlatformDto startPlatform, List<PlatformDto> platforms) {
        if (startPlatform == null || platforms == null || platforms.isEmpty()) {
            return false;
        }

        if (!platforms.contains(startPlatform)) {
            return false;
        }

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
                if (!visited.contains(next)
                        && platformReachabilityService.canMoveFromTo(current, next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return visited;
    }
}