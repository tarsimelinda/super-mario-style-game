package com.codecool.backend.service;

import com.codecool.backend.dto.EnemyCreateRequest;
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
class EnemyServiceTest {

    @Mock
    EnemyRepository repository;

    @InjectMocks
    EnemyService service;

    @Test
    void getAll_returnsEnemies() {
        when(repository.findAll()).thenReturn(List.of(new Enemy()));

        var result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void create_mapsFieldsCorrectly() {
        EnemyCreateRequest req = new EnemyCreateRequest(10, 5, 20, "red", true);

        when(repository.save(any(Enemy.class))).thenAnswer(inv -> inv.getArgument(0));

        Enemy e = service.create(req);

        assertEquals("red", e.getColor());
        assertEquals(20, e.getHp());
    }

    @Test
    void deleteZeroHp_callsRepository() {
        when(repository.deleteByHp(0)).thenReturn(5L);

        long deleted = service.deleteZeroHp();

        assertEquals(5, deleted);
    }
}
