package com.codecool.backend.service;

import com.codecool.backend.dto.UserResponse;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse updateCheckpoint(String id, Integer checkpoint) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        user.setCheckpoint(checkpoint);

        return toResponse(repository.save(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getCheckpoint(),
                user.getCharacter()
        );
    }
}