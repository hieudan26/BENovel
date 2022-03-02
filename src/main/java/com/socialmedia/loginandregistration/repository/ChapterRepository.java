package com.socialmedia.loginandregistration.repository;

import com.socialmedia.loginandregistration.model.Entity.Chapter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository  extends MongoRepository<Chapter, ObjectId> {
    List<Chapter> findByDautruyenId(ObjectId id);
}