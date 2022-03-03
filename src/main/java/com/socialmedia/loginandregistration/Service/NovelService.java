package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.model.Entity.Novel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NovelService {
    List<Novel> getNovels();
    Novel findByName(String name);
    List<Novel> getNovels(Pageable pageable);
    List<Novel> findAllByStatus(String status,Pageable pageable);
    List<Novel> SearchByTentruyen(String value,Pageable pageable);
    List<Novel> SearchByTypeAndTentruyen(String type,String value,Pageable pageable);
    List<Novel> SearchByTacgia(String value,Pageable pageable);
    List<Novel> SearchByType(String theloai,Pageable pageable);
}
