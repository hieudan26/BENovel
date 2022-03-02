package com.socialmedia.loginandregistration.repository;

import java.util.Optional;

import com.socialmedia.loginandregistration.model.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}