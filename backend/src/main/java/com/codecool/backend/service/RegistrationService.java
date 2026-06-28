package com.codecool.backend.service;

import com.codecool.backend.dto.RegistrationCreateRequest;
import com.codecool.backend.dto.RegistrationResponse;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.model.Player;
import com.codecool.backend.model.PlayerStatus;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.PlayerRepository;
import com.codecool.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private static final int DEFAULT_CHECKPOINT = 1;
    private static final int DEFAULT_HP = 3;
    private static final int DEFAULT_COINS = 0;

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final CharacterOptionService characterOptionService;

    public RegistrationService(
            UserRepository userRepository,
            PlayerRepository playerRepository,
            CharacterOptionService characterOptionService
    ) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.characterOptionService = characterOptionService;
    }

    @Transactional
    public RegistrationResponse register(RegistrationCreateRequest body) {
        String name = normalizeName(body.name());
        String characterKey = normalizeCharacterKey(body.character());

        CharacterOption character = characterOptionService.getByKey(characterKey);

        User savedUser = createUser(name, characterKey);
        Player savedPlayer = createPlayer(savedUser.getId());

        return toResponse(savedUser, savedPlayer, character);
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    private String normalizeCharacterKey(String character) {
        return character.trim().toLowerCase();
    }

    private User createUser(String name, String characterKey) {
        User user = new User();
        user.setName(name);
        user.setCharacter(characterKey);
        user.setCheckpoint(DEFAULT_CHECKPOINT);

        return userRepository.save(user);
    }

    private Player createPlayer(String userId) {
        Player player = new Player();
        player.setUserId(userId);
        player.setHp(DEFAULT_HP);
        player.setCoins(DEFAULT_COINS);
        player.setStatus(PlayerStatus.PLAYING);

        return playerRepository.save(player);
    }

    private RegistrationResponse toResponse(
            User user,
            Player player,
            CharacterOption character
    ) {
        return new RegistrationResponse(
                user.getId(),
                player.getId(),
                user.getName(),
                user.getCharacter(),
                character.getColor(),
                user.getCheckpoint(),
                player.getHp(),
                player.getCoins(),
                player.getStatus()
        );
    }
}