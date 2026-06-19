package com.codecool.backend.service;

import com.codecool.backend.dto.CharacterCreateRequest;
import com.codecool.backend.dto.CharacterOptionResponse;
import com.codecool.backend.model.CharacterOption;
import com.codecool.backend.repository.CharacterOptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterOptionServiceTest {

    @Mock
    private CharacterOptionRepository repository;

    @InjectMocks
    private CharacterOptionService service;

    @Test
    void getAllShouldReturnAllCharacterOptionsAsResponses() {
        CharacterOption chicken = createCharacterOption("1", "chicken", "Chicken", "yellow");
        CharacterOption duck = createCharacterOption("2", "duck", "Duck", "white");

        when(repository.findAll()).thenReturn(List.of(chicken, duck));

        List<CharacterOptionResponse> result = service.getAll();

        assertEquals(2, result.size());

        assertEquals("1", result.get(0).id());
        assertEquals("chicken", result.get(0).key());
        assertEquals("Chicken", result.get(0).displayName());
        assertEquals("yellow", result.get(0).color());

        assertEquals("2", result.get(1).id());
        assertEquals("duck", result.get(1).key());
        assertEquals("Duck", result.get(1).displayName());
        assertEquals("white", result.get(1).color());

        verify(repository).findAll();
    }

    @Test
    void createShouldNormalizeKeyTrimFieldsSaveCharacterAndReturnResponse() {
        CharacterCreateRequest request = new CharacterCreateRequest(
                "  CHICKEN  ",
                "  Chicken  ",
                "  yellow  "
        );

        CharacterOption savedCharacter = createCharacterOption("1", "chicken", "Chicken", "yellow");

        when(repository.existsByKey("chicken")).thenReturn(false);
        when(repository.save(any(CharacterOption.class))).thenReturn(savedCharacter);

        CharacterOptionResponse result = service.create(request);

        assertEquals("1", result.id());
        assertEquals("chicken", result.key());
        assertEquals("Chicken", result.displayName());
        assertEquals("yellow", result.color());

        ArgumentCaptor<CharacterOption> captor = ArgumentCaptor.forClass(CharacterOption.class);
        verify(repository).save(captor.capture());

        CharacterOption characterToSave = captor.getValue();

        assertEquals("chicken", characterToSave.getKey());
        assertEquals("Chicken", characterToSave.getDisplayName());
        assertEquals("yellow", characterToSave.getColor());

        verify(repository).existsByKey("chicken");
    }

    @Test
    void createShouldThrowExceptionWhenCharacterAlreadyExists() {
        CharacterCreateRequest request = new CharacterCreateRequest(
                "  CHICKEN  ",
                "Chicken",
                "yellow"
        );

        when(repository.existsByKey("chicken")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(request)
        );

        assertEquals("Character already exists: chicken", exception.getMessage());

        verify(repository).existsByKey("chicken");
        verify(repository, never()).save(any(CharacterOption.class));
    }

    @Test
    void getByKeyShouldReturnCharacterByNormalizedKey() {
        CharacterOption character = createCharacterOption("1", "chicken", "Chicken", "yellow");

        when(repository.findByKey("chicken")).thenReturn(Optional.of(character));

        CharacterOption result = service.getByKey("  CHICKEN  ");

        assertEquals(character, result);

        verify(repository).findByKey("chicken");
    }

    @Test
    void getByKeyShouldThrowExceptionWhenKeyIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getByKey(null)
        );

        assertEquals("Character is required", exception.getMessage());

        verifyNoInteractions(repository);
    }

    @Test
    void getByKeyShouldThrowExceptionWhenKeyIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getByKey("   ")
        );

        assertEquals("Character is required", exception.getMessage());

        verifyNoInteractions(repository);
    }

    @Test
    void getByKeyShouldThrowExceptionWhenCharacterDoesNotExist() {
        when(repository.findByKey("dragon")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getByKey("Dragon")
        );

        assertEquals("Invalid character: Dragon", exception.getMessage());

        verify(repository).findByKey("dragon");
    }

    private CharacterOption createCharacterOption(
            String id,
            String key,
            String displayName,
            String color
    ) {
        CharacterOption character = new CharacterOption();
        character.setId(id);
        character.setKey(key);
        character.setDisplayName(displayName);
        character.setColor(color);
        return character;
    }
}