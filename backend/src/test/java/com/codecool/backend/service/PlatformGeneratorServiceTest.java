package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.PlatformDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformGeneratorServiceTest {

    @Mock
    private PlatformReachabilityService platformReachabilityService;

    @Mock
    private RandomService randomService;

    @Mock
    private GameBalanceProperties gameBalanceProperties;

    @Mock
    private GameBalanceProperties.Platform platformBalance;

    @InjectMocks
    private PlatformGeneratorService service;

    @Test
    void generatePlatformsShouldAlwaysIncludeStartPlatform() {
        mockPlatformCount(1, 1);

        List<PlatformDto> result = service.generatePlatforms();

        assertTrue(result.contains(startPlatform()));
    }

    @Test
    void generatePlatformsShouldReturnPlatformsSortedByX() {
        mockPlatformCount(3, 3);

        when(platformReachabilityService.canMoveFromTo(any(PlatformDto.class), any(PlatformDto.class)))
                .thenReturn(true);

        when(randomService.betweenInclusive(anyInt(), anyInt()))
                .thenReturn(
                        3,

                        70, 300, 300,
                        70, 600, 200,
                        70, 900, 250,
                        70, 1100, 150
                );

        List<PlatformDto> result = service.generatePlatforms();

        List<PlatformDto> sorted = result.stream()
                .sorted(Comparator.comparingInt(PlatformDto::x))
                .toList();

        assertEquals(sorted, result);
    }

    @Test
    void generatePlatformsShouldUseConfiguredMinAndMaxPlatformCount() {
        mockPlatformCount(4, 7);

        when(platformReachabilityService.canMoveFromTo(any(PlatformDto.class), any(PlatformDto.class)))
                .thenReturn(false);

        when(randomService.betweenInclusive(anyInt(), anyInt()))
                .thenReturn(4);

        service.generatePlatforms();

        verify(randomService).betweenInclusive(4, 7);
    }

    @Test
    void generatePlatformsShouldNotAddUnreachableBasePlatforms() {
        mockPlatformCount(6, 6);

        when(platformReachabilityService.canMoveFromTo(any(PlatformDto.class), any(PlatformDto.class)))
                .thenReturn(false);

        when(randomService.betweenInclusive(anyInt(), anyInt()))
                .thenReturn(
                        6,

                        70, 300, 300,
                        70, 600, 250,
                        70, 800, 200,
                        70, 1000, 150,
                        70, 1200, 100
                );

        List<PlatformDto> result = service.generatePlatforms();

        assertEquals(List.of(startPlatform()), result);
    }

    @Test
    void generatePlatformsShouldNotReturnPlatformsTouchingOrBelowGround() {
        mockPlatformCount(6, 6);

        when(randomService.betweenInclusive(anyInt(), anyInt()))
                .thenReturn(
                        6,

                        70, 300, GameConstants.GROUND_Y,
                        70, 600, GameConstants.GROUND_Y,
                        70, 800, GameConstants.GROUND_Y,
                        70, 1000, GameConstants.GROUND_Y,
                        70, 1200, GameConstants.GROUND_Y
                );

        List<PlatformDto> result = service.generatePlatforms();

        assertEquals(List.of(startPlatform()), result);
        verifyNoInteractions(platformReachabilityService);
    }

    private void mockPlatformCount(int minCount, int maxCount) {
        when(gameBalanceProperties.platform()).thenReturn(platformBalance);
        when(platformBalance.minCount()).thenReturn(minCount);
        when(platformBalance.maxCount()).thenReturn(maxCount);
    }

    private PlatformDto startPlatform() {
        return new PlatformDto(80, 460, 220, GameConstants.PLATFORM_HEIGHT);
    }
}