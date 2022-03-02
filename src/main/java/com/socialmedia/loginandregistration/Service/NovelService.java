package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.model.Entity.Novel;

import java.util.List;

public interface NovelService {
    List<Novel> getNovels();
    Novel findByName(String name);
}
