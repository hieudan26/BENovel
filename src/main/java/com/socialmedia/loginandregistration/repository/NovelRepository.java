package com.socialmedia.loginandregistration.repository;

import com.socialmedia.loginandregistration.model.Entity.Novel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NovelRepository  extends MongoRepository<Novel, ObjectId> {
    Optional<Novel> findByTentruyen(String name);
}
