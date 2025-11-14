package com.codecool.backend.service;

import com.codecool.backend.dto.UserCreateRequest;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService service;

    @Test
    void getAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new User()));

        var result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void create_setsDefaultCheckpointIfNull() {
        UserCreateRequest req = new UserCreateRequest("Alice", null, "mage");

        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User u = service.create(req);

        assertEquals(1, u.getCheckpoint());
    }
}
