package com.codecool.backend.service;

import com.codecool.backend.dto.UserResponse;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void updateCheckpointShouldUpdateUserCheckpoint() {
        User existingUser = createUser(
                "user-1",
                "Melinda",
                1,
                "blue"
        );

        User savedUser = createUser(
                "user-1",
                "Melinda",
                3,
                "blue"
        );

        when(repository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = service.updateCheckpoint("user-1", 3);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());

        User userToSave = userCaptor.getValue();

        assertEquals(3, userToSave.getCheckpoint());

        assertEquals("user-1", response.id());
        assertEquals("Melinda", response.name());
        assertEquals(3, response.checkpoint());
        assertEquals("blue", response.character());
    }

    @Test
    void updateCheckpointShouldAllowCheckpointOne() {
        User existingUser = createUser(
                "user-1",
                "Melinda",
                5,
                "blue"
        );

        User savedUser = createUser(
                "user-1",
                "Melinda",
                1,
                "blue"
        );

        when(repository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = service.updateCheckpoint("user-1", 1);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());

        assertEquals(1, userCaptor.getValue().getCheckpoint());
        assertEquals(1, response.checkpoint());
    }

    @Test
    void updateCheckpointShouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(repository.findById("missing-user")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.updateCheckpoint("missing-user", 2)
        );

        assertEquals("User not found: missing-user", exception.getMessage());

        verify(repository).findById("missing-user");
        verify(repository, never()).save(any(User.class));
    }

    private User createUser(
            String id,
            String name,
            int checkpoint,
            String character
    ) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setCheckpoint(checkpoint);
        user.setCharacter(character);
        return user;
    }
}
