package com.codecool.backend.controller;

import com.codecool.backend.dto.PlayerCreateRequest;
import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.Player;
import com.codecool.backend.repository.PlayerRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayersController {

    private final PlayerRepository repository;

    public PlayersController(PlayerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Player> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{name}")
    public Player getByName(@PathVariable String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Player not found: " + name));
    }

    @PostMapping
    public ResponseEntity<Player> create(@Valid @RequestBody PlayerCreateRequest body) {
        Player p = new Player();
        p.setName(body.name());
        p.setHp(body.hp());
        p.setCoins(body.coins());
        p.setShield(Boolean.TRUE.equals(body.shield()));
        p.setStatus("playing");
        Player saved = repository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{name}")
    public Player patch(@PathVariable String name, @RequestBody PlayerPatchRequest body) {
        Player p = repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Player not found: " + name));

        if (body.hp() != null)     p.setHp(body.hp());
        if (body.coins() != null)  p.setCoins(body.coins());
        if (body.shield() != null) p.setShield(body.shield());
        if (body.status() != null) p.setStatus(body.status());

        return repository.save(p);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByStatus(@RequestParam(required = false) String status) {
        if (status == null || status.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        repository.deleteByStatus(status);
        return ResponseEntity.noContent().build();
    }
}
