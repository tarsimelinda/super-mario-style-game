package com.codecool.backend.service;

import com.codecool.backend.dto.RegistrationCreateRequest;
import com.codecool.backend.dto.RegistrationResponse;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.model.Player;
import com.codecool.backend.model.PlayerStatus;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.PlayerRepository;
import com.codecool.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CharacterOptionService characterOptionService;

    @InjectMocks
    private RegistrationService service;

    @Test
    void registerShouldCreateUserAndPlayerWithDefaultValues() {
        RegistrationCreateRequest request = new RegistrationCreateRequest(
                "Melinda",
                "blue"
        );

        CharacterOption character = createCharacterOption("blue", "#0000ff");

        User savedUser = createUser(
                "user-1",
                "Melinda",
                "blue",
                1
        );

        Player savedPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                0,
                PlayerStatus.PLAYING
        );

        when(characterOptionService.getByKey("blue")).thenReturn(character);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        RegistrationResponse response = service.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);

        verify(userRepository).save(userCaptor.capture());
        verify(playerRepository).save(playerCaptor.capture());

        User userToSave = userCaptor.getValue();
        Player playerToSave = playerCaptor.getValue();

        assertEquals("Melinda", userToSave.getName());
        assertEquals("blue", userToSave.getCharacter());
        assertEquals(1, userToSave.getCheckpoint());

        assertEquals("user-1", playerToSave.getUserId());
        assertEquals(3, playerToSave.getHp());
        assertEquals(0, playerToSave.getCoins());
        assertEquals(PlayerStatus.PLAYING, playerToSave.getStatus());

        assertEquals("user-1", response.userId());
        assertEquals("player-1", response.playerId());
        assertEquals("Melinda", response.name());
        assertEquals("blue", response.character());
        assertEquals("#0000ff", response.characterColor());
        assertEquals(1, response.checkpoint());
        assertEquals(3, response.hp());
        assertEquals(0, response.coins());
        assertEquals(PlayerStatus.PLAYING, response.status());
    }

    @Test
    void registerShouldTrimNameAndNormalizeCharacterKey() {
        RegistrationCreateRequest request = new RegistrationCreateRequest(
                "  Melinda  ",
                "  BLUE  "
        );

        CharacterOption character = createCharacterOption("blue", "#0000ff");

        User savedUser = createUser(
                "user-1",
                "Melinda",
                "blue",
                1
        );

        Player savedPlayer = createPlayer(
                "player-1",
                "user-1",
                3,
                0,
                PlayerStatus.PLAYING
        );

        when(characterOptionService.getByKey("blue")).thenReturn(character);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        RegistrationResponse response = service.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User userToSave = userCaptor.getValue();

        assertEquals("Melinda", userToSave.getName());
        assertEquals("blue", userToSave.getCharacter());

        verify(characterOptionService).getByKey("blue");

        assertEquals("Melinda", response.name());
        assertEquals("blue", response.character());
    }

    @Test
    void registerShouldNotCreateUserOrPlayerWhenCharacterDoesNotExist() {
        RegistrationCreateRequest request = new RegistrationCreateRequest(
                "Melinda",
                "unknown"
        );

        when(characterOptionService.getByKey("unknown"))
                .thenThrow(new RuntimeException("Character not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.register(request)
        );

        assertEquals("Character not found", exception.getMessage());

        verify(characterOptionService).getByKey("unknown");
        verifyNoInteractions(userRepository, playerRepository);
    }

    @Test
    void registerShouldNotCreatePlayerWhenUserSaveFails() {
        RegistrationCreateRequest request = new RegistrationCreateRequest(
                "Melinda",
                "blue"
        );

        CharacterOption character = createCharacterOption("blue", "#0000ff");

        when(characterOptionService.getByKey("blue")).thenReturn(character);
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("User save failed"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.register(request)
        );

        assertEquals("User save failed", exception.getMessage());

        verify(characterOptionService).getByKey("blue");
        verify(userRepository).save(any(User.class));
        verifyNoInteractions(playerRepository);
    }

    @Test
    void registerShouldThrowExceptionWhenPlayerSaveFails() {
        RegistrationCreateRequest request = new RegistrationCreateRequest(
                "Melinda",
                "blue"
        );

        CharacterOption character = createCharacterOption("blue", "#0000ff");

        User savedUser = createUser(
                "user-1",
                "Melinda",
                "blue",
                1
        );

        when(characterOptionService.getByKey("blue")).thenReturn(character);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(playerRepository.save(any(Player.class)))
                .thenThrow(new RuntimeException("Player save failed"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.register(request)
        );

        assertEquals("Player save failed", exception.getMessage());

        verify(userRepository).save(any(User.class));
        verify(playerRepository).save(any(Player.class));
    }

    private CharacterOption createCharacterOption(String key, String color) {
        CharacterOption character = new CharacterOption();
        character.setKey(key);
        character.setColor(color);
        return character;
    }

    private User createUser(
            String id,
            String name,
            String character,
            int checkpoint
    ) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setCharacter(character);
        user.setCheckpoint(checkpoint);
        return user;
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