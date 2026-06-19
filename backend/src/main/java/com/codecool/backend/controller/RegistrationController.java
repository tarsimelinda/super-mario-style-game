package com.codecool.backend.controller;

import com.codecool.backend.config.ApiRoutes;
import com.codecool.backend.dto.RegistrationCreateRequest;
import com.codecool.backend.dto.RegistrationResponse;
import com.codecool.backend.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.REGISTRATIONS)
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse register(@Valid @RequestBody RegistrationCreateRequest body) {
        return service.register(body);
    }
}