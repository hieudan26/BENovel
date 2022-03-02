package com.socialmedia.loginandregistration.Service.Impl;

import com.socialmedia.loginandregistration.Service.ChapterService;
import com.socialmedia.loginandregistration.model.Entity.Chapter;
import com.socialmedia.loginandregistration.model.Entity.Novel;
import com.socialmedia.loginandregistration.repository.ChapterRepository;
import org.bson.types.ObjectId;

import java.util.List;

public class ChapterServiceImpl implements ChapterService {
    private ChapterRepository chapterRepository;
    @Override
    public List<Chapter> findByDauTruyen(ObjectId id) {
        return chapterRepository.findByDautruyenId(id);
    }
}
