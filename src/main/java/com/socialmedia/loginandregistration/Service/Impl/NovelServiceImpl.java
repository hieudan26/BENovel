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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        log.info("Fetching all Novels ");
        return novelRepository.findAll();
    }

    @Override
    public List<Novel> getNovels(Pageable pageable) {
        log.info("Fetching all Novels page: "+pageable.getPageNumber()+" page size: "+pageable.getPageSize());
        return novelRepository.findAllBy_idNotNull(pageable);
    }



    @Override
    public Novel findByName(String name) {
        log.info("Fetching  Novel: "+name);
        return novelRepository.findByTentruyen(name).get();
    }

    @Override
    public List<Novel> findAllByStatus(String status, Pageable pageable) {
        log.info("Fetching  Novel status: "+status);
        return novelRepository.findAllByStatus(status,pageable);
    }

    @Override
    public List<Novel> SearchByTentruyen(String value,Pageable pageable) {
        log.info("Searching  Novel value: "+value);
        return novelRepository.findAllByTentruyenContainsAllIgnoreCase(value, pageable);
    }

    @Override
    public List<Novel> SearchByTypeAndTentruyen(String type, String value, Pageable pageable) {
        log.info("Searching  Novel type: "+type+" value: "+value);
        return novelRepository.findAllByTheloaiContainsAndTentruyenContainsAllIgnoreCase(type,value, pageable);
    }

    @Override
    public List<Novel> SearchByTacgia(String value, Pageable pageable) {
        log.info("Searching Novel value: "+value);
        return novelRepository.findAllByTacgiaContainsAllIgnoreCase(value, pageable);
    }

    @Override
    public List<Novel> SearchByType(String theloai, Pageable pageable) {
        log.info("Searching Novel by theloai: "+theloai);
        return novelRepository.findAllByTheloaiContainsAllIgnoreCase(theloai, pageable);
    }
}
