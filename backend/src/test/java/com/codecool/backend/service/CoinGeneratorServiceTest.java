package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.PlatformDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinGeneratorServiceTest {

    @Mock
    private RandomService randomService;

    @Mock
    private CoinValidator coinValidator;

    @Mock
    private GameBalanceProperties gameBalanceProperties;

    @Mock
    private GameBalanceProperties.Coin coinProperties;

    @InjectMocks
    private CoinGeneratorService service;

    @Test
    void generateCoinsShouldReturnEmptyListWhenTargetCoinCountIsZero() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        mockCoinBalance(0, 0);
        when(randomService.betweenInclusive(0, 0)).thenReturn(0);

        List<CoinDto> result = service.generateCoins(List.of(platform));

        assertTrue(result.isEmpty());

        verify(randomService).betweenInclusive(0, 0);
        verifyNoInteractions(coinValidator);
    }

    @Test
    void generateCoinsShouldGenerateOneCoinAbovePlatformWhenCoinIsValid() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        mockCoinBalance(1, 1);

        when(randomService.betweenInclusive(1, 1)).thenReturn(1);
        when(randomService.betweenInclusive(1, 3)).thenReturn(1);
        when(randomService.betweenInclusive(35, 80)).thenReturn(50);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(platform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(platform));

        assertEquals(1, result.size());

        CoinDto coin = result.get(0);

        assertEquals(platform.centerX() - GameConstants.COIN_WIDTH / 2, coin.x());
        assertEquals(platform.top() - 50 - GameConstants.COIN_HEIGHT, coin.y());
        assertEquals(GameConstants.COIN_WIDTH, coin.width());
        assertEquals(GameConstants.COIN_HEIGHT, coin.height());

        verify(coinValidator).isValidCoinPosition(any(CoinDto.class), eq(List.of(platform)));
    }

    @Test
    void generateCoinsShouldNotAddCoinWhenValidatorRejectsPosition() {
        PlatformDto platform = createPlatform(100, 300, 200, 30);

        mockCoinBalance(1, 1);

        when(randomService.betweenInclusive(1, 1)).thenReturn(1);
        when(randomService.betweenInclusive(1, 3)).thenReturn(1);
        when(randomService.betweenInclusive(35, 80)).thenReturn(50);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(platform))))
                .thenReturn(false);

        List<CoinDto> result = service.generateCoins(List.of(platform));

        assertTrue(result.isEmpty());

        verify(coinValidator).isValidCoinPosition(any(CoinDto.class), eq(List.of(platform)));
    }

    @Test
    void generateCoinsShouldUseZeroOrOneCoinForSmallPlatform() {
        PlatformDto smallPlatform = createPlatform(100, 300, 90, 30);

        mockCoinBalance(1, 1);

        when(randomService.betweenInclusive(1, 1)).thenReturn(1);
        when(randomService.betweenInclusive(0, 1)).thenReturn(1);
        when(randomService.betweenInclusive(35, 80)).thenReturn(40);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(smallPlatform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(smallPlatform));

        assertEquals(1, result.size());

        verify(randomService).betweenInclusive(0, 1);
    }

    @Test
    void generateCoinsShouldUseOneOrTwoCoinsForMediumPlatform() {
        PlatformDto mediumPlatform = createPlatform(100, 300, 120, 30);

        mockCoinBalance(1, 1);

        when(randomService.betweenInclusive(1, 1)).thenReturn(1);
        when(randomService.betweenInclusive(1, 2)).thenReturn(1);
        when(randomService.betweenInclusive(35, 80)).thenReturn(40);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(mediumPlatform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(mediumPlatform));

        assertEquals(1, result.size());

        verify(randomService).betweenInclusive(1, 2);
    }

    @Test
    void generateCoinsShouldUseOneToThreeCoinsForLargePlatform() {
        PlatformDto largePlatform = createPlatform(100, 300, 200, 30);

        mockCoinBalance(1, 1);

        when(randomService.betweenInclusive(1, 1)).thenReturn(1);
        when(randomService.betweenInclusive(1, 3)).thenReturn(1);
        when(randomService.betweenInclusive(35, 80)).thenReturn(40);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(largePlatform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(largePlatform));

        assertEquals(1, result.size());

        verify(randomService).betweenInclusive(1, 3);
    }

    @Test
    void generateCoinsShouldNotGenerateMoreCoinsThanTargetCoinCount() {
        PlatformDto platform = createPlatform(100, 300, 250, 30);

        mockCoinBalance(2, 2);

        when(randomService.betweenInclusive(2, 2)).thenReturn(2);
        when(randomService.betweenInclusive(1, 3)).thenReturn(3);
        when(randomService.betweenInclusive(-8, 8)).thenReturn(0);
        when(randomService.betweenInclusive(35, 80)).thenReturn(50);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(platform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(platform));

        assertEquals(2, result.size());
    }

    @Test
    void generateCoinsShouldReturnCoinsSortedByXPosition() {
        PlatformDto leftPlatform = createPlatform(100, 300, 200, 30);
        PlatformDto rightPlatform = createPlatform(500, 300, 200, 30);

        mockCoinBalance(2, 2);

        when(randomService.betweenInclusive(2, 2)).thenReturn(2);
        when(randomService.betweenInclusive(1, 3)).thenReturn(1);
        when(randomService.betweenInclusive(35, 80)).thenReturn(50);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(leftPlatform, rightPlatform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(leftPlatform, rightPlatform));

        assertEquals(2, result.size());
        assertTrue(result.get(0).x() <= result.get(1).x());
    }

    @Test
    void generateCoinsShouldAvoidOverlappingExistingCoins() {
        PlatformDto platform = createPlatform(100, 300, 250, 30);

        mockCoinBalance(2, 2);

        when(randomService.betweenInclusive(2, 2)).thenReturn(2);
        when(randomService.betweenInclusive(1, 3)).thenReturn(2);

        when(randomService.betweenInclusive(-8, 8)).thenReturn(0);
        when(randomService.betweenInclusive(35, 80)).thenReturn(50);

        when(coinValidator.isValidCoinPosition(any(CoinDto.class), eq(List.of(platform))))
                .thenReturn(true);

        List<CoinDto> result = service.generateCoins(List.of(platform));

        assertEquals(2, result.size());

        CoinDto first = result.get(0);
        CoinDto second = result.get(1);

        boolean overlaps = first.left() < second.right()
                && first.right() > second.left()
                && first.top() < second.bottom()
                && first.bottom() > second.top();

        assertFalse(overlaps);
    }

    private void mockCoinBalance(int minCount, int maxCount) {
        when(gameBalanceProperties.coin()).thenReturn(coinProperties);
        when(coinProperties.minCount()).thenReturn(minCount);
        when(coinProperties.maxCount()).thenReturn(maxCount);
    }

    private PlatformDto createPlatform(int x, int y, int width, int height) {
        return new PlatformDto(x, y, width, height);
    }
}