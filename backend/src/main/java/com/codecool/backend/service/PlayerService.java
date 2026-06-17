package com.codecool.backend.service;

import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Player patchById(String id, PlayerPatchRequest body) {
        Player p = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Player not found: " + id));

        applyPatch(p, body);

        return repository.save(p);
    }

    private void applyPatch(Player p, PlayerPatchRequest body) {
        if (body.hp() != null) {
            p.setHp(Math.max(0, body.hp()));
        }

        if (body.coins() != null) {
            p.setCoins(Math.max(0, body.coins()));
        }

        if (body.status() != null) {
            p.setStatus(body.status());
        }
    }

}