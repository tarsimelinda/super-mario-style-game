package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.dto.PointDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelServiceTest {

    @Mock
    private PlatformGeneratorService platformGeneratorService;

    @Mock
    private CoinGeneratorService coinGeneratorService;

    @Mock
    private LevelValidatorService levelValidatorService;

    @Mock
    private LevelDefaultsService levelDefaultsService;

    @Mock
    private EnemySpawnService enemySpawnService;

    @Mock
    private GameBalanceProperties gameBalanceProperties;

    @Mock
    private GameBalanceProperties.Level levelBalance;

    @InjectMocks
    private LevelService service;

    @Test
    void getDefaultLevelShouldDelegateToLevelDefaultsService() {
        LevelDto defaultLevel = new LevelDto(
                new PointDto(150, GameConstants.GROUND_Y - GameConstants.PLAYER_HEIGHT),
                GameConstants.DEFAULT_LIVES,
                List.of(),
                List.of(),
                List.of()
        );

        when(levelDefaultsService.getDefaultLevel()).thenReturn(defaultLevel);

        LevelDto result = service.getDefaultLevel();

        assertEquals(defaultLevel, result);
        verify(levelDefaultsService).getDefaultLevel();
        verifyNoInteractions(
                platformGeneratorService,
                coinGeneratorService,
                levelValidatorService,
                enemySpawnService,
                gameBalanceProperties
        );
    }

    @Test
    void getRandomLevelShouldReturnGeneratedLevelWhenPlayableOnFirstAttempt() {
        mockRandomLevelAttempts(3);

        List<PlatformDto> platforms = List.of(
                new PlatformDto(100, 450, 200, GameConstants.PLATFORM_HEIGHT)
        );

        List<CoinDto> coins = List.of(
                new CoinDto(150, 410, GameConstants.COIN_WIDTH, GameConstants.COIN_HEIGHT)
        );

        List<EnemySpawnDto> enemies = List.of();

        when(platformGeneratorService.generatePlatforms()).thenReturn(platforms);
        when(levelValidatorService.isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                platforms
        )).thenReturn(true);
        when(coinGeneratorService.generateCoins(platforms)).thenReturn(coins);
        when(enemySpawnService.generateEnemies(platforms)).thenReturn(enemies);

        LevelDto result = service.getRandomLevel();

        assertEquals(LevelDefaultsService.RANDOM_LEVEL_PLAYER_START, result.playerStart());
        assertEquals(GameConstants.DEFAULT_LIVES, result.lives());
        assertEquals(platforms, result.platforms());
        assertEquals(enemies, result.enemies());
        assertEquals(coins, result.coins());

        verify(platformGeneratorService).generatePlatforms();
        verify(levelValidatorService).isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                platforms
        );
        verify(coinGeneratorService).generateCoins(platforms);
        verify(enemySpawnService).generateEnemies(platforms);
        verify(levelDefaultsService, never()).getDefaultLevel();
    }

    @Test
    void getRandomLevelShouldRetryUntilPlayableLevelIsGenerated() {
        mockRandomLevelAttempts(3);

        List<PlatformDto> invalidPlatforms = List.of(
                new PlatformDto(100, 450, 200, GameConstants.PLATFORM_HEIGHT)
        );

        List<PlatformDto> validPlatforms = List.of(
                new PlatformDto(300, 350, 200, GameConstants.PLATFORM_HEIGHT)
        );

        List<CoinDto> coins = List.of();
        List<EnemySpawnDto> enemies = List.of();

        when(platformGeneratorService.generatePlatforms())
                .thenReturn(invalidPlatforms)
                .thenReturn(validPlatforms);

        when(levelValidatorService.isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                invalidPlatforms
        )).thenReturn(false);

        when(levelValidatorService.isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                validPlatforms
        )).thenReturn(true);

        when(coinGeneratorService.generateCoins(validPlatforms)).thenReturn(coins);
        when(enemySpawnService.generateEnemies(validPlatforms)).thenReturn(enemies);

        LevelDto result = service.getRandomLevel();

        assertEquals(validPlatforms, result.platforms());
        assertEquals(enemies, result.enemies());
        assertEquals(coins, result.coins());

        verify(platformGeneratorService, times(2)).generatePlatforms();
        verify(levelValidatorService).isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                invalidPlatforms
        );
        verify(levelValidatorService).isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                validPlatforms
        );
        verify(coinGeneratorService).generateCoins(validPlatforms);
        verify(enemySpawnService).generateEnemies(validPlatforms);
        verify(levelDefaultsService, never()).getDefaultLevel();
    }

    @Test
    void getRandomLevelShouldReturnDefaultLevelWhenNoPlayableLevelIsGenerated() {
        mockRandomLevelAttempts(2);

        List<PlatformDto> firstPlatforms = List.of(
                new PlatformDto(100, 450, 200, GameConstants.PLATFORM_HEIGHT)
        );

        List<PlatformDto> secondPlatforms = List.of(
                new PlatformDto(300, 350, 200, GameConstants.PLATFORM_HEIGHT)
        );

        LevelDto defaultLevel = new LevelDto(
                new PointDto(150, GameConstants.GROUND_Y - GameConstants.PLAYER_HEIGHT),
                GameConstants.DEFAULT_LIVES,
                List.of(),
                List.of(),
                List.of()
        );

        when(platformGeneratorService.generatePlatforms())
                .thenReturn(firstPlatforms)
                .thenReturn(secondPlatforms);

        when(levelValidatorService.isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                firstPlatforms
        )).thenReturn(false);

        when(levelValidatorService.isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                secondPlatforms
        )).thenReturn(false);

        when(levelDefaultsService.getDefaultLevel()).thenReturn(defaultLevel);

        LevelDto result = service.getRandomLevel();

        assertEquals(defaultLevel, result);

        verify(platformGeneratorService, times(2)).generatePlatforms();
        verify(levelValidatorService).isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                firstPlatforms
        );
        verify(levelValidatorService).isPlayable(
                LevelDefaultsService.RANDOM_LEVEL_START_PLATFORM,
                secondPlatforms
        );
        verify(levelDefaultsService).getDefaultLevel();

        verifyNoInteractions(coinGeneratorService, enemySpawnService);
    }

    private void mockRandomLevelAttempts(int attempts) {
        when(gameBalanceProperties.level()).thenReturn(levelBalance);
        when(levelBalance.randomLevelAttempts()).thenReturn(attempts);
    }
}
