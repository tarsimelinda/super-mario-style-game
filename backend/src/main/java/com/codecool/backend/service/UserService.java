package com.codecool.backend.service;

import com.codecool.backend.dto.UserCreateRequest;
import com.codecool.backend.exception.NotFoundException;
import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User create(UserCreateRequest body) {
        User u = new User();
        u.setName(body.name());
        u.setCharacter(body.character());
        u.setCheckpoint(body.checkpoint() != null ? body.checkpoint() : 1);
        return repository.save(u);
    }

    public User updateCheckpoint(String id, int checkpoint) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        user.setCheckpoint(Math.max(1, checkpoint));

        return repository.save(user);
    }
}