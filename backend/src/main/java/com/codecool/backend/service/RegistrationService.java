package com.codecool.backend.service;

import com.codecool.backend.dto.RegistrationCreateRequest;
import com.codecool.backend.dto.RegistrationResponse;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.model.Player;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.PlayerRepository;
import com.codecool.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

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

    public RegistrationResponse register(RegistrationCreateRequest body) {
        String name = body.name().trim();
        String characterKey = body.character().trim().toLowerCase();

        CharacterOption character = characterOptionService.getByKey(characterKey);

        User user = new User();
        user.setName(name);
        user.setCharacter(characterKey);
        user.setCheckpoint(1);

        User savedUser = userRepository.save(user);

        Player player = new Player();
        player.setUserId(savedUser.getId());
        player.setName(name);
        player.setHp(3);
        player.setCoins(0);
        player.setStatus("playing");

        Player savedPlayer = playerRepository.save(player);

        return new RegistrationResponse(
                savedUser.getId(),
                savedPlayer.getId(),
                savedUser.getName(),
                savedUser.getCharacter(),
                character.getColor(),
                savedUser.getCheckpoint(),
                savedPlayer.getHp(),
                savedPlayer.getCoins(),
                savedPlayer.getStatus()
        );
    }
}
