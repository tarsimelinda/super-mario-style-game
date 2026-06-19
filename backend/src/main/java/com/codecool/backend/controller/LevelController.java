package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.service.LevelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.LEVELS)
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping(ApiRoutes.RANDOM)
    public LevelDto getRandomLevel() {
        return levelService.getRandomLevel();
    }
}