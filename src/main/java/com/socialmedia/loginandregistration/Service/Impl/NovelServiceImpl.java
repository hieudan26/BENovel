package com.socialmedia.loginandregistration.Service.Impl;

import com.socialmedia.loginandregistration.Service.NovelService;
import com.socialmedia.loginandregistration.Service.UserService;

import com.socialmedia.loginandregistration.model.Entity.Novel;
import com.socialmedia.loginandregistration.model.Entity.Role;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.repository.NovelRepository;
import com.socialmedia.loginandregistration.repository.RoleRepository;
import com.socialmedia.loginandregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class NovelServiceImpl implements NovelService {
    final NovelRepository novelRepository;
    @Override
    public List<Novel> getNovels(){
        log.info("Fetching all users ");
        return novelRepository.findAll();
    }

    @Override
    public Novel findByName(String name) {
        return novelRepository.findByTentruyen(name).get();
    }
}
