package com.codecool.backend.controller;

import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayersController.class)
class PlayersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    void getAllPlayers_shouldReturnList() throws Exception {
        Player player = new Player("1", "Mario", 100, 10, false, "playing");
        when(playerRepository.findAll()).thenReturn(List.of(player));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Mario"))
                .andExpect(jsonPath("$[0].hp").value(100))
                .andExpect(jsonPath("$[0].status").value("playing"));
    }

    @Test
    void createPlayer_shouldSetStatusPlaying_andReturn201() throws Exception {
        Player saved = new Player("2", "Luigi", 80, 5, false, "playing");
        when(playerRepository.save(any(Player.class))).thenReturn(saved);

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Luigi",
                                  "hp": 80,
                                  "coins": 5,
                                  "shield": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Luigi"))
                .andExpect(jsonPath("$.status").value("playing"));
    }

    @Test
    void patchPlayer_shouldSetAbsoluteValues() throws Exception {
        Player existing = new Player("3", "Peach", 100, 10, false, "playing");
        Player updated  = new Player("3", "Peach", 95, 15, true,  "menu");

        when(playerRepository.findByName("Peach")).thenReturn(Optional.of(existing));
        when(playerRepository.save(any(Player.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/players/Peach")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hp": 95,
                                  "coins": 15,
                                  "shield": true,
                                  "status": "menu"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hp").value(95))
                .andExpect(jsonPath("$.coins").value(15))
                .andExpect(jsonPath("$.shield").value(true))
                .andExpect(jsonPath("$.status").value("menu"));
    }

    @Test
    void patchPlayer_shouldReturn404IfNotFound() throws Exception {
        when(playerRepository.findByName("Bowser")).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/players/Bowser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hp": 10
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Player not found: Bowser"));
    }

    @Test
    void deletePlayersByStatus_shouldReturn204() throws Exception {
        when(playerRepository.deleteByStatus("playing")).thenReturn(2L);

        mockMvc.perform(delete("/api/players")
                        .param("status", "playing"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
