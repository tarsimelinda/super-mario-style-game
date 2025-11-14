package com.codecool.backend.service;

import com.codecool.backend.dto.PlayerCreateRequest;
import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    PlayerRepository repository;

    @InjectMocks
    PlayerService service;

    @Test
    void getAll_returnsPlayers() {
        when(repository.findAll()).thenReturn(List.of(new Player(), new Player()));

        var result = service.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void getByName_throwsException_ifNotFound() {
        when(repository.findByName("Bob")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getByName("Bob"));
    }

    @Test
    void create_savesPlayerCorrectly() {
        PlayerCreateRequest req = new PlayerCreateRequest("Mario", 100, 10, true);

        Player saved = new Player();
        saved.setName("Mario");

        when(repository.save(any(Player.class))).thenReturn(saved);

        Player result = service.create(req);

        assertEquals("Mario", result.getName());
    }

    @Test
    void patch_updatesFieldsCorrectly() {
        Player existing = new Player();
        existing.setName("Mario");
        existing.setHp(100);
        existing.setCoins(10);
        existing.setShield(false);

        when(repository.findByName("Mario")).thenReturn(Optional.of(existing));
        when(repository.save(any(Player.class))).thenAnswer(inv -> inv.getArgument(0));

        PlayerPatchRequest patch = new PlayerPatchRequest(50, 20, true, "playing");

        Player updated = service.patch("Mario", patch);

        assertEquals(50, updated.getHp());
        assertEquals(20, updated.getCoins());
        assertTrue(updated.getShield());
    }
}
