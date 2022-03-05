package mobile.repository;

import mobile.model.Entity.Chapter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface ChapterRepository  extends MongoRepository<Chapter, ObjectId> {
    List<Chapter> findByDautruyenId(ObjectId id);
    Optional<Chapter> findByDautruyenIdAndChapnumber(ObjectId id,int number);
    List<Chapter> findAllByDautruyenId(ObjectId id, Pageable pageable);

}