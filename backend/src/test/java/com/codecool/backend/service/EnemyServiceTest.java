package com.codecool.backend.service;

import com.codecool.backend.dto.EnemyCreateRequest;
import com.codecool.backend.dto.EnemyResponse;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnemyServiceTest {

    @Mock
    private EnemyRepository repository;

    @InjectMocks
    private EnemyService service;

    @Test
    void createShouldSaveEnemyAndReturnResponse() {
        EnemyCreateRequest request = new EnemyCreateRequest(
                10,
                3,
                100,
                "  red  ",
                true
        );

        Enemy savedEnemy = createEnemy("1", 10, 3, 100, "red", true);

        when(repository.save(any(Enemy.class))).thenReturn(savedEnemy);

        EnemyResponse result = service.create(request);

        assertEquals("1", result.id());
        assertEquals(10, result.damage());
        assertEquals(3, result.speed());
        assertEquals(100, result.hp());
        assertEquals("red", result.color());
        assertTrue(result.canJump());

        ArgumentCaptor<Enemy> captor = ArgumentCaptor.forClass(Enemy.class);
        verify(repository).save(captor.capture());

        Enemy enemyToSave = captor.getValue();

        assertEquals(10, enemyToSave.getDamage());
        assertEquals(3, enemyToSave.getSpeed());
        assertEquals(100, enemyToSave.getHp());
        assertEquals("red", enemyToSave.getColor());
        assertTrue(enemyToSave.isCanJump());
    }

    @Test
    void createShouldSetCanJumpToFalseWhenRequestCanJumpIsFalse() {
        EnemyCreateRequest request = new EnemyCreateRequest(
                10,
                3,
                100,
                "blue",
                false
        );

        Enemy savedEnemy = createEnemy("1", 10, 3, 100, "blue", false);

        when(repository.save(any(Enemy.class))).thenReturn(savedEnemy);

        EnemyResponse result = service.create(request);

        assertFalse(result.canJump());

        ArgumentCaptor<Enemy> captor = ArgumentCaptor.forClass(Enemy.class);
        verify(repository).save(captor.capture());

        assertFalse(captor.getValue().isCanJump());
    }

    @Test
    void createShouldSetCanJumpToFalseWhenRequestCanJumpIsNull() {
        EnemyCreateRequest request = new EnemyCreateRequest(
                10,
                3,
                100,
                "green",
                null
        );

        Enemy savedEnemy = createEnemy("1", 10, 3, 100, "green", false);

        when(repository.save(any(Enemy.class))).thenReturn(savedEnemy);

        EnemyResponse result = service.create(request);

        assertFalse(result.canJump());

        ArgumentCaptor<Enemy> captor = ArgumentCaptor.forClass(Enemy.class);
        verify(repository).save(captor.capture());

        assertFalse(captor.getValue().isCanJump());
    }

    @Test
    void createShouldTrimColorBeforeSaving() {
        EnemyCreateRequest request = new EnemyCreateRequest(
                5,
                2,
                50,
                "  purple  ",
                true
        );

        Enemy savedEnemy = createEnemy("1", 5, 2, 50, "purple", true);

        when(repository.save(any(Enemy.class))).thenReturn(savedEnemy);

        EnemyResponse result = service.create(request);

        assertEquals("purple", result.color());

        ArgumentCaptor<Enemy> captor = ArgumentCaptor.forClass(Enemy.class);
        verify(repository).save(captor.capture());

        assertEquals("purple", captor.getValue().getColor());
    }

    private Enemy createEnemy(
            String id,
            int damage,
            int speed,
            int hp,
            String color,
            boolean canJump
    ) {
        Enemy enemy = new Enemy();
        enemy.setId(id);
        enemy.setDamage(damage);
        enemy.setSpeed(speed);
        enemy.setHp(hp);
        enemy.setColor(color);
        enemy.setCanJump(canJump);
        return enemy;
    }
}