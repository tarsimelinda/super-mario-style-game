package com.codecool.backend.controller;

import com.codecool.backend.dto.PlayerCreateRequest;
import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.model.Player;
import com.codecool.backend.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayersController {

    private final PlayerService service;

    public PlayersController(PlayerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Player> getAll() {
        return service.getAll();
    }

    @GetMapping("/{name}")
    public Player getByName(@PathVariable String name) {
        return service.getByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Player create(@Valid @RequestBody PlayerCreateRequest body) {
        return service.create(body);
    }

    @PatchMapping("/{name}")
    public Player patch(@PathVariable String name, @RequestBody PlayerPatchRequest body) {
        return service.patch(name, body);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByStatus(@RequestParam String status) {
        service.deleteByStatus(status);
    }
}
