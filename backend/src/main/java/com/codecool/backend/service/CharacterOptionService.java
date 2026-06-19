package com.codecool.backend.service;

import com.codecool.backend.dto.CharacterCreateRequest;
import com.codecool.backend.dto.CharacterOptionResponse;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.repository.CharacterOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterOptionService {

    private final CharacterOptionRepository repository;

    public CharacterOptionService(CharacterOptionRepository repository) {
        this.repository = repository;
    }

    public List<CharacterOptionResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CharacterOptionResponse create(CharacterCreateRequest body) {
        String key = normalizeKey(body.key());

        if (repository.existsByKey(key)) {
            throw new IllegalArgumentException("Character already exists: " + key);
        }

        CharacterOption character = new CharacterOption();
        character.setKey(key);
        character.setDisplayName(body.displayName().trim());
        character.setColor(body.color().trim());

        return toResponse(repository.save(character));
    }

    public CharacterOption getByKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Character is required");
        }

        String normalizedKey = normalizeKey(key);

        return repository.findByKey(normalizedKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid character: " + key));
    }

    private CharacterOptionResponse toResponse(CharacterOption character) {
        return new CharacterOptionResponse(
                character.getId(),
                character.getKey(),
                character.getDisplayName(),
                character.getColor()
        );
    }

    private String normalizeKey(String key) {
        return key.trim().toLowerCase();
    }
}