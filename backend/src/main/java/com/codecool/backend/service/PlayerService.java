package com.codecool.backend.service;

import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.dto.PlayerResponse;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public PlayerResponse patchById(String id, PlayerPatchRequest body) {
        Player player = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Player not found: " + id));

        applyPatch(player, body);

        return toResponse(repository.save(player));
    }

    private void applyPatch(Player player, PlayerPatchRequest body) {
        if (body.hp() != null) {
            player.setHp(Math.max(0, body.hp()));
        }

        if (body.coins() != null) {
            player.setCoins(Math.max(0, body.coins()));
        }

        if (body.status() != null) {
            player.setStatus(body.status());
        }
    }

    private PlayerResponse toResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getUserId(),
                player.getHp(),
                player.getCoins(),
                player.getStatus()
        );
    }
}