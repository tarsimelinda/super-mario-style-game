package com.codecool.backend.controller;

import com.codecool.backend.dto.PlayerCreateRequest;
import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.model.Player;
import com.codecool.backend.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/players")
public class PlayersController {

    private final PlayerService service;

    public PlayersController(PlayerService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Player create(@Valid @RequestBody PlayerCreateRequest body) {
        return service.create(body);
    }

    @PatchMapping("/{id}")
    public Player patch(@PathVariable String id, @RequestBody PlayerPatchRequest body) {
        return service.patchById(id, body);
    }
}