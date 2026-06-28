package com.codecool.backend.service;

import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.PlatformDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelValidatorServiceTest {

    @Mock
    private PlatformReachabilityService platformReachabilityService;

    @InjectMocks
    private LevelValidatorService service;

    @Test
    void isPlayableShouldReturnFalseWhenStartPlatformIsNull() {
        List<PlatformDto> platforms = List.of(
                platform(100, 450, 200)
        );

        boolean result = service.isPlayable(null, platforms);

        assertFalse(result);
        verifyNoInteractions(platformReachabilityService);
    }

    @Test
    void isPlayableShouldReturnFalseWhenPlatformsAreNull() {
        PlatformDto startPlatform = platform(100, 450, 200);

        boolean result = service.isPlayable(startPlatform, null);

        assertFalse(result);
        verifyNoInteractions(platformReachabilityService);
    }

    @Test
    void isPlayableShouldReturnFalseWhenPlatformsAreEmpty() {
        PlatformDto startPlatform = platform(100, 450, 200);

        boolean result = service.isPlayable(startPlatform, List.of());

        assertFalse(result);
        verifyNoInteractions(platformReachabilityService);
    }

    @Test
    void isPlayableShouldReturnFalseWhenStartPlatformIsNotInPlatforms() {
        PlatformDto startPlatform = platform(100, 450, 200);
        PlatformDto otherPlatform = platform(400, 350, 150);

        boolean result = service.isPlayable(startPlatform, List.of(otherPlatform));

        assertFalse(result);
        verifyNoInteractions(platformReachabilityService);
    }

    @Test
    void isPlayableShouldReturnTrueWhenOnlyStartPlatformExists() {
        PlatformDto startPlatform = platform(100, 450, 200);

        boolean result = service.isPlayable(startPlatform, List.of(startPlatform));

        assertTrue(result);
        verifyNoInteractions(platformReachabilityService);
    }

    @Test
    void isPlayableShouldReturnTrueWhenAllPlatformsAreReachable() {
        PlatformDto startPlatform = platform(100, 450, 200);
        PlatformDto secondPlatform = platform(350, 400, 150);
        PlatformDto thirdPlatform = platform(600, 350, 150);

        List<PlatformDto> platforms = List.of(startPlatform, secondPlatform, thirdPlatform);

        when(platformReachabilityService.canMoveFromTo(startPlatform, secondPlatform))
                .thenReturn(true);
        when(platformReachabilityService.canMoveFromTo(startPlatform, thirdPlatform))
                .thenReturn(false);
        when(platformReachabilityService.canMoveFromTo(secondPlatform, thirdPlatform))
                .thenReturn(true);

        boolean result = service.isPlayable(startPlatform, platforms);

        assertTrue(result);

        verify(platformReachabilityService).canMoveFromTo(startPlatform, secondPlatform);
        verify(platformReachabilityService).canMoveFromTo(startPlatform, thirdPlatform);
        verify(platformReachabilityService).canMoveFromTo(secondPlatform, thirdPlatform);
    }

    @Test
    void isPlayableShouldReturnFalseWhenAtLeastOnePlatformIsUnreachable() {
        PlatformDto startPlatform = platform(100, 450, 200);
        PlatformDto reachablePlatform = platform(350, 400, 150);
        PlatformDto unreachablePlatform = platform(900, 100, 150);

        List<PlatformDto> platforms = List.of(
                startPlatform,
                reachablePlatform,
                unreachablePlatform
        );

        when(platformReachabilityService.canMoveFromTo(startPlatform, reachablePlatform))
                .thenReturn(true);
        when(platformReachabilityService.canMoveFromTo(startPlatform, unreachablePlatform))
                .thenReturn(false);
        when(platformReachabilityService.canMoveFromTo(reachablePlatform, unreachablePlatform))
                .thenReturn(false);

        boolean result = service.isPlayable(startPlatform, platforms);

        assertFalse(result);

        verify(platformReachabilityService).canMoveFromTo(startPlatform, reachablePlatform);
        verify(platformReachabilityService).canMoveFromTo(startPlatform, unreachablePlatform);
        verify(platformReachabilityService).canMoveFromTo(reachablePlatform, unreachablePlatform);
    }

    private PlatformDto platform(int x, int y, int width) {
        return new PlatformDto(x, y, width, GameConstants.PLATFORM_HEIGHT);
    }
}
