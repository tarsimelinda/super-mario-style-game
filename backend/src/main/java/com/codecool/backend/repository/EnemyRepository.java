package com.codecool.backend.repository;

import com.codecool.backend.model.Enemy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemyRepository extends MongoRepository<Enemy, String> {
}
