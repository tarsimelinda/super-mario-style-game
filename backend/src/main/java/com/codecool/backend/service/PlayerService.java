package com.codecool.backend.service;

import com.codecool.backend.dto.PlayerCreateRequest;
import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    private static final Set<String> ALLOWED_STATUS = Set.of("playing", "menu", "dead");

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> getAll() {
        return repository.findAll();
    }

    public Player getByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Player not found: " + name));
    }

    public Player create(PlayerCreateRequest body) {
        Player p = new Player();
        p.setName(body.name());
        p.setHp(body.hp());
        p.setCoins(body.coins());
        p.setShield(Boolean.TRUE.equals(body.shield()));
        p.setStatus("playing");
        return repository.save(p);
    }

    public Player patch(String name, PlayerPatchRequest body) {
        Player p = getByName(name);

        if (body.hp() != null) p.setHp(Math.max(0, body.hp()));
        if (body.coins() != null) p.setCoins(Math.max(0, body.coins()));
        if (body.shield() != null) p.setShield(body.shield());
        if (body.status() != null) {
            if (!ALLOWED_STATUS.contains(body.status())) {
                throw new IllegalArgumentException("Invalid status value");
            }
            p.setStatus(body.status());
        }
        return repository.save(p);
    }

    public void deleteByStatus(String status) {
        repository.deleteByStatus(status);
    }
}
