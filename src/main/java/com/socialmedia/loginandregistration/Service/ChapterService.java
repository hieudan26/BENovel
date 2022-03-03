package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.model.Entity.Chapter;
import com.socialmedia.loginandregistration.model.Entity.Novel;
import org.apache.catalina.LifecycleState;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChapterService {
    List<Chapter> findByDauTruyen(ObjectId id);
    Chapter findByDauTruyenAndChapterNumber(ObjectId id,int number);
    List<Chapter> findByDauTruyen(ObjectId id, Pageable pageable);
}
