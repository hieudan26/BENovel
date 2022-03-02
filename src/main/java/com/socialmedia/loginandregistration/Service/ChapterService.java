package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.model.Entity.Chapter;
import com.socialmedia.loginandregistration.model.Entity.Novel;
import org.apache.catalina.LifecycleState;
import org.bson.types.ObjectId;

import java.util.List;

public interface ChapterService {
    List<Chapter> findByDauTruyen(ObjectId id);
}
