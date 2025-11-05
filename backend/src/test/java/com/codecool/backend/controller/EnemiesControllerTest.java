package com.codecool.backend.controller;

import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnemiesController.class)
class EnemiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnemyRepository enemyRepository;

    @Test
    void getAllEnemies_shouldReturnList() throws Exception {
        Enemy enemy = new Enemy("1", 5, 2, 10, "red", false);
        when(enemyRepository.findAll()).thenReturn(List.of(enemy));

        mockMvc.perform(get("/api/enemies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].color").value("red"))
                .andExpect(jsonPath("$[0].hp").value(10));
    }

    @Test
    void createEnemy_shouldSaveAndReturnEnemy_with201() throws Exception {
        Enemy enemy = new Enemy("2", 7, 3, 20, "green", true);
        when(enemyRepository.save(any(Enemy.class))).thenReturn(enemy);

        mockMvc.perform(post("/api/enemies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "damage": 7,
                                  "speed": 3,
                                  "hp": 20,
                                  "color": "green",
                                  "canJump": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.color").value("green"))
                .andExpect(jsonPath("$.canJump").value(true));
    }

    @Test
    void deleteZeroHp_shouldReturnMessage() throws Exception {
        when(enemyRepository.deleteByHp(0)).thenReturn(1L);

        mockMvc.perform(delete("/api/enemies/zero-hp"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted enemies: 1"));
    }
}
