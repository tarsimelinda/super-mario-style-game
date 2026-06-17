package com.codecool.backend.service;

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
}