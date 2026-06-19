package com.codecool.backend.service;

import com.codecool.backend.config.GameBalanceProperties;
import com.codecool.backend.config.GameConstants;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnemySpawnServiceTest {

    @Mock
    private EnemyRepository enemyRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private GameBalanceProperties gameBalanceProperties;

    @Mock
    private GameBalanceProperties.Enemy enemyProperties;

    @InjectMocks
    private EnemySpawnService service;

    @Test
    void generateEnemiesShouldReturnEmptyListWhenThereAreNoEnemyTypes() {
        PlatformDto platform = createPlatform(100, 300, 120, 30);

        when(enemyRepository.findAll()).thenReturn(List.of());

        List<EnemySpawnDto> result = service.generateEnemies(List.of(platform));

        assertTrue(result.isEmpty());

        verify(enemyRepository).findAll();
        verifyNoInteractions(randomService);
        verifyNoInteractions(gameBalanceProperties);
    }

    @Test
    void generateEnemiesShouldSkipFirstPlatform() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        PlatformDto firstPlatform = createPlatform(100, 300, 150, 30);
        PlatformDto secondPlatform = createPlatform(400, 300, 150, 30);

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(1, 10);
        when(enemyProperties.spawnChancePercent()).thenReturn(100);

        when(randomService.percent()).thenReturn(50);
        when(randomService.pickOne(List.of(enemy))).thenReturn(enemy);

        List<EnemySpawnDto> result = service.generateEnemies(List.of(firstPlatform, secondPlatform));

        assertEquals(1, result.size());

        EnemySpawnDto spawnedEnemy = result.get(0);

        assertEquals(
                secondPlatform.x() + secondPlatform.width() / 2 - GameConstants.ENEMY_WIDTH / 2,
                spawnedEnemy.x()
        );

        verify(randomService, times(1)).percent();
        verify(randomService, times(1)).pickOne(List.of(enemy));
    }

    @Test
    void generateEnemiesShouldNotSpawnOnTooNarrowPlatform() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        PlatformDto firstPlatform = createPlatform(100, 300, 150, 30);
        PlatformDto narrowPlatform = createPlatform(400, 300, 80, 30);

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(1, 10);

        List<EnemySpawnDto> result = service.generateEnemies(List.of(firstPlatform, narrowPlatform));

        assertTrue(result.isEmpty());

        verify(randomService, never()).percent();
        verify(randomService, never()).pickOne(anyList());
    }

    @Test
    void generateEnemiesShouldNotSpawnWhenPlatformIsTooHighForEnemy() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        PlatformDto firstPlatform = createPlatform(100, 300, 150, 30);
        PlatformDto tooHighPlatform = createPlatform(400, 50, 150, 30);

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(1, 10);

        List<EnemySpawnDto> result = service.generateEnemies(List.of(firstPlatform, tooHighPlatform));

        assertTrue(result.isEmpty());

        verify(randomService, never()).percent();
        verify(randomService, never()).pickOne(anyList());
    }

    @Test
    void generateEnemiesShouldNotSpawnWhenPlatformIsTooLowForEnemy() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        PlatformDto firstPlatform = createPlatform(100, 300, 150, 30);
        PlatformDto tooLowPlatform = createPlatform(400, 500, 150, 30);

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(1, 10);

        List<EnemySpawnDto> result = service.generateEnemies(List.of(firstPlatform, tooLowPlatform));

        assertTrue(result.isEmpty());

        verify(randomService, never()).percent();
        verify(randomService, never()).pickOne(anyList());
    }

    @Test
    void generateEnemiesShouldNotSpawnWhenRandomPercentIsNotBelowSpawnChance() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        PlatformDto firstPlatform = createPlatform(100, 300, 150, 30);
        PlatformDto spawnPlatform = createPlatform(400, 300, 150, 30);

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(1, 10);
        when(enemyProperties.spawnChancePercent()).thenReturn(50);

        when(randomService.percent()).thenReturn(50);

        List<EnemySpawnDto> result = service.generateEnemies(List.of(firstPlatform, spawnPlatform));

        assertTrue(result.isEmpty());

        verify(randomService).percent();
        verify(randomService, never()).pickOne(anyList());
    }

    @Test
    void generateEnemiesShouldSpawnWhenRandomPercentIsBelowSpawnChance() {
        Enemy enemy = createEnemy(3, 10, 100, "red", true);

        PlatformDto firstPlatform = createPlatform(100, 300, 150, 30);
        PlatformDto spawnPlatform = createPlatform(400, 300, 150, 30);

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(1, 10);
        when(enemyProperties.spawnChancePercent()).thenReturn(50);

        when(randomService.percent()).thenReturn(49);
        when(randomService.pickOne(List.of(enemy))).thenReturn(enemy);

        List<EnemySpawnDto> result = service.generateEnemies(List.of(firstPlatform, spawnPlatform));

        assertEquals(1, result.size());

        EnemySpawnDto spawnedEnemy = result.get(0);

        assertEquals(spawnPlatform.x() + spawnPlatform.width() / 2 - GameConstants.ENEMY_WIDTH / 2, spawnedEnemy.x());
        assertEquals(spawnPlatform.y() - GameConstants.ENEMY_HEIGHT, spawnedEnemy.y());
        assertEquals(GameConstants.ENEMY_WIDTH, spawnedEnemy.width());
        assertEquals(GameConstants.ENEMY_HEIGHT, spawnedEnemy.height());

        assertEquals(3, spawnedEnemy.speed());
        assertEquals(10, spawnedEnemy.damage());
        assertEquals(100, spawnedEnemy.hp());
        assertEquals("red", spawnedEnemy.color());
        assertTrue(spawnedEnemy.canJump());
    }

    @Test
    void generateEnemiesShouldNotSpawnMoreEnemiesThanCalculatedEnemyCount() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        List<PlatformDto> platforms = List.of(
                createPlatform(0, 300, 150, 30),
                createPlatform(100, 300, 150, 30),
                createPlatform(200, 300, 150, 30),
                createPlatform(300, 300, 150, 30),
                createPlatform(400, 300, 150, 30),
                createPlatform(500, 300, 150, 30),
                createPlatform(600, 300, 150, 30),
                createPlatform(700, 300, 150, 30),
                createPlatform(800, 300, 150, 30),
                createPlatform(900, 300, 150, 30),
                createPlatform(1000, 300, 150, 30),
                createPlatform(1100, 300, 150, 30)
        );

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(0, 10);
        when(enemyProperties.spawnChancePercent()).thenReturn(100);

        when(randomService.percent()).thenReturn(0);
        when(randomService.pickOne(List.of(enemy))).thenReturn(enemy);

        List<EnemySpawnDto> result = service.generateEnemies(platforms);

        assertEquals(2, result.size());

        verify(randomService, times(2)).pickOne(List.of(enemy));
    }

    @Test
    void generateEnemiesShouldRespectMaximumEnemyCount() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        List<PlatformDto> platforms = List.of(
                createPlatform(0, 300, 150, 30),
                createPlatform(100, 300, 150, 30),
                createPlatform(200, 300, 150, 30),
                createPlatform(300, 300, 150, 30),
                createPlatform(400, 300, 150, 30),
                createPlatform(500, 300, 150, 30),
                createPlatform(600, 300, 150, 30),
                createPlatform(700, 300, 150, 30),
                createPlatform(800, 300, 150, 30),
                createPlatform(900, 300, 150, 30),
                createPlatform(1000, 300, 150, 30),
                createPlatform(1100, 300, 150, 30)
        );

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(0, 1);
        when(enemyProperties.spawnChancePercent()).thenReturn(100);

        when(randomService.percent()).thenReturn(0);
        when(randomService.pickOne(List.of(enemy))).thenReturn(enemy);

        List<EnemySpawnDto> result = service.generateEnemies(platforms);

        assertEquals(1, result.size());
    }

    @Test
    void generateEnemiesShouldRespectMinimumEnemyCountWhenEnoughPlatformsPassFilters() {
        Enemy enemy = createEnemy(3, 10, 100, "red", false);

        List<PlatformDto> platforms = List.of(
                createPlatform(0, 300, 150, 30),
                createPlatform(100, 300, 150, 30),
                createPlatform(200, 300, 150, 30)
        );

        when(enemyRepository.findAll()).thenReturn(List.of(enemy));
        mockEnemyCounts(2, 10);
        when(enemyProperties.spawnChancePercent()).thenReturn(100);

        when(randomService.percent()).thenReturn(0);
        when(randomService.pickOne(List.of(enemy))).thenReturn(enemy);

        List<EnemySpawnDto> result = service.generateEnemies(platforms);

        assertEquals(2, result.size());
    }

    private void mockEnemyCounts(int minCount, int maxCount) {
        when(gameBalanceProperties.enemy()).thenReturn(enemyProperties);
        when(enemyProperties.minCount()).thenReturn(minCount);
        when(enemyProperties.maxCount()).thenReturn(maxCount);
    }

    private Enemy createEnemy(
            int speed,
            int damage,
            int hp,
            String color,
            boolean canJump
    ) {
        Enemy enemy = new Enemy();
        enemy.setSpeed(speed);
        enemy.setDamage(damage);
        enemy.setHp(hp);
        enemy.setColor(color);
        enemy.setCanJump(canJump);
        return enemy;
    }

    private PlatformDto createPlatform(int x, int y, int width, int height) {
        return new PlatformDto(x, y, width, height);
    }
}