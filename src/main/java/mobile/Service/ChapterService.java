package mobile.Service;

import mobile.model.Entity.Chapter;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChapterService {
    List<Chapter> findByDauTruyen(ObjectId id);
    Chapter findByDauTruyenAndChapterNumber(ObjectId id,int number);
    List<Chapter> findByDauTruyen(ObjectId id, Pageable pageable);
}
