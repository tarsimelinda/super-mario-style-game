package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.PlayerPatchRequest;
import com.codecool.backend.dto.PlayerResponse;
import com.codecool.backend.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.PLAYERS)
public class PlayersController {

    private final PlayerService service;

    public PlayersController(PlayerService service) {
        this.service = service;
    }

    @PatchMapping(ApiRoutes.BY_ID)
    public PlayerResponse patch(
            @PathVariable String id,
            @Valid @RequestBody PlayerPatchRequest body
    ) {
        return service.patchById(id, body);
    }
}