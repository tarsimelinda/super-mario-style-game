package com.codecool.backend.service;

import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.dto.PlayerResponse;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.Player;
import com.codecool.backend.model.PlayerStatus;
import com.codecool.backend.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository repository;

    @InjectMocks
    private PlayerService service;

    @Test
    void patchByIdShouldUpdateAllProvidedFields() {
        Player existingPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                10,
                PlayerStatus.PLAYING
        );

        Player savedPlayer = createPlayer(
                "player-1",
                "user-1",
                2,
                25,
                PlayerStatus.DEAD
        );

        PlayerPatchRequest request = new PlayerPatchRequest(
                2,
                25,
                PlayerStatus.DEAD
        );

        when(repository.findById("player-1")).thenReturn(Optional.of(existingPlayer));
        when(repository.save(any(Player.class))).thenReturn(savedPlayer);

        PlayerResponse response = service.patchById("player-1", request);

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(repository).save(playerCaptor.capture());

        Player playerToSave = playerCaptor.getValue();

        assertEquals(2, playerToSave.getHp());
        assertEquals(25, playerToSave.getCoins());
        assertEquals(PlayerStatus.DEAD, playerToSave.getStatus());

        assertEquals("player-1", response.id());
        assertEquals("user-1", response.userId());
        assertEquals(2, response.hp());
        assertEquals(25, response.coins());
        assertEquals(PlayerStatus.DEAD, response.status());
    }

    @Test
    void patchByIdShouldOnlyUpdateProvidedFields() {
        Player existingPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                10,
                PlayerStatus.PLAYING
        );

        Player savedPlayer = createPlayer(
                "player-1",
                "user-1",
                1,
                10,
                PlayerStatus.PLAYING
        );

        PlayerPatchRequest request = new PlayerPatchRequest(
                1,
                null,
                null
        );

        when(repository.findById("player-1")).thenReturn(Optional.of(existingPlayer));
        when(repository.save(any(Player.class))).thenReturn(savedPlayer);

        PlayerResponse response = service.patchById("player-1", request);

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(repository).save(playerCaptor.capture());

        Player playerToSave = playerCaptor.getValue();

        assertEquals(1, playerToSave.getHp());
        assertEquals(10, playerToSave.getCoins());
        assertEquals(PlayerStatus.PLAYING, playerToSave.getStatus());

        assertEquals(1, response.hp());
        assertEquals(10, response.coins());
        assertEquals(PlayerStatus.PLAYING, response.status());
    }

    @Test
    void patchByIdShouldClampNegativeHpAndCoinsToZero() {
        Player existingPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                10,
                PlayerStatus.PLAYING
        );

        Player savedPlayer = createPlayer(
                "player-1",
                "user-1",
                0,
                0,
                PlayerStatus.PLAYING
        );

        PlayerPatchRequest request = new PlayerPatchRequest(
                -5,
                -20,
                null
        );

        when(repository.findById("player-1")).thenReturn(Optional.of(existingPlayer));
        when(repository.save(any(Player.class))).thenReturn(savedPlayer);

        PlayerResponse response = service.patchById("player-1", request);

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(repository).save(playerCaptor.capture());

        Player playerToSave = playerCaptor.getValue();

        assertEquals(0, playerToSave.getHp());
        assertEquals(0, playerToSave.getCoins());
        assertEquals(PlayerStatus.PLAYING, playerToSave.getStatus());

        assertEquals(0, response.hp());
        assertEquals(0, response.coins());
        assertEquals(PlayerStatus.PLAYING, response.status());
    }

    @Test
    void patchByIdShouldKeepExistingValuesWhenRequestFieldsAreNull() {
        Player existingPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                10,
                PlayerStatus.PLAYING
        );

        Player savedPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                10,
                PlayerStatus.PLAYING
        );

        PlayerPatchRequest request = new PlayerPatchRequest(
                null,
                null,
                null
        );

        when(repository.findById("player-1")).thenReturn(Optional.of(existingPlayer));
        when(repository.save(any(Player.class))).thenReturn(savedPlayer);

        PlayerResponse response = service.patchById("player-1", request);

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(repository).save(playerCaptor.capture());

        Player playerToSave = playerCaptor.getValue();

        assertEquals(3, playerToSave.getHp());
        assertEquals(10, playerToSave.getCoins());
        assertEquals(PlayerStatus.PLAYING, playerToSave.getStatus());

        assertEquals(3, response.hp());
        assertEquals(10, response.coins());
        assertEquals(PlayerStatus.PLAYING, response.status());
    }

    @Test
    void patchByIdShouldThrowNotFoundExceptionWhenPlayerDoesNotExist() {
        when(repository.findById("missing-player")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.patchById(
                        "missing-player",
                        new PlayerPatchRequest(1, 5, PlayerStatus.PLAYING)
                )
        );

        assertEquals("Player not found: missing-player", exception.getMessage());

        verify(repository).findById("missing-player");
        verify(repository, never()).save(any(Player.class));
    }

    private Player createPlayer(
            String id,
            String userId,
            int hp,
            int coins,
            PlayerStatus status
    ) {
        Player player = new Player();
        player.setId(id);
        player.setUserId(userId);
        player.setHp(hp);
        player.setCoins(coins);
        player.setStatus(status);
        return player;
    }
}