package com.codecool.backend.repository;

import com.codecool.backend.model.CharacterOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterOptionRepository extends MongoRepository<CharacterOption, String> {
    boolean existsByKey(String key);
}