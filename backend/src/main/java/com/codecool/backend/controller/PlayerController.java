package com.codecool.backend.controller;

import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PlayerController {

    private final PlayerRepository repository;

    public PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/player")
    public List<Player> getAllPlayers() {
        return repository.findAll();
    }

    @PostMapping("/player/post")
    public Player createPlayer(@RequestBody Player player) {
        player.setStatus("playing");
        return repository.save(player);
    }


    @PatchMapping("/player/patch/{name}")
    public Player updatePlayer(@PathVariable String name, @RequestBody Player updatedData) {
        Optional<Player> optionalPlayer = repository.findByName(name);
        if (optionalPlayer.isEmpty()) {
            throw new RuntimeException("Player not found");
        }
        Player player = optionalPlayer.get();

        if (updatedData.getHp() != 0) {
            player.setHp(player.getHp() - updatedData.getHp());
        }
        if (updatedData.getCoins() != 0) {
            player.setCoins(player.getCoins() + updatedData.getCoins());
        }
        player.setShield(updatedData.isShield());

        return repository.save(player);
    }

    @DeleteMapping("/player/delete")
    public String deletePlayingPlayers() {
        List<Player> toDelete = repository.findAll().stream()
                .filter(p -> "playing".equals(p.getStatus()))
                .toList();
        repository.deleteAll(toDelete);
        return "Players with status 'playing' deleted.";
    }
}
