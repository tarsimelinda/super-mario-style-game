package com.codecool.backend.repository;

import com.codecool.backend.model.CharacterOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterOptionRepository extends MongoRepository<CharacterOption, String> {
    boolean existsByKey(String key);

    Optional<CharacterOption> findByKey(String key);
}