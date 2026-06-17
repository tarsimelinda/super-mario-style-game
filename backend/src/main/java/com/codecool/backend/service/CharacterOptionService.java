package com.codecool.backend.service;

import com.codecool.backend.dto.CharacterCreateRequest;
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

    public List<CharacterOption> getAll() {
        List<CharacterOption> characters = repository.findAll();

        if (!characters.isEmpty()) {
            return characters;
        }

        return getFallbackCharacters();
    }

    public CharacterOption create(CharacterCreateRequest body) {
        String key = body.key().trim().toLowerCase();

        if (repository.existsByKey(key)) {
            throw new IllegalArgumentException("Character already exists: " + key);
        }

        CharacterOption character = new CharacterOption();
        character.setKey(key);
        character.setDisplayName(body.displayName().trim());
        character.setColor(body.color().trim());

        return repository.save(character);
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }

        String normalizedKey = key.trim().toLowerCase();

        if (repository.existsByKey(normalizedKey)) {
            return true;
        }

        return getFallbackCharacters().stream()
                .anyMatch(character -> character.getKey().equals(normalizedKey));
    }

    private List<CharacterOption> getFallbackCharacters() {
        return List.of(
                new CharacterOption(null, "lilly", "Lilly", "pink"),
                new CharacterOption(null, "ponyo", "Ponyo", "orange"),
                new CharacterOption(null, "balu", "Balu", "blue"),
                new CharacterOption(null, "momo", "Momo", "purple")
        );
    }

    public CharacterOption getByKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Character is required");
        }

        String normalizedKey = key.trim().toLowerCase();

        return repository.findAll().stream()
                .filter(character -> character.getKey().equals(normalizedKey))
                .findFirst()
                .orElseGet(() ->
                        getFallbackCharacters().stream()
                                .filter(character -> character.getKey().equals(normalizedKey))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Invalid character: " + key))
                );
    }
}