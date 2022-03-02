package com.socialmedia.loginandregistration.controller;

import com.socialmedia.loginandregistration.Service.NovelService;
import com.socialmedia.loginandregistration.Service.UserService;
import com.socialmedia.loginandregistration.mapping.UserMapping;
import com.socialmedia.loginandregistration.model.Entity.Novel;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.RegisterRequest;
import com.socialmedia.loginandregistration.model.payload.request.RoleToUserRequest;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/novels")
@RequiredArgsConstructor
public class NovelResource {
    private static final Logger LOGGER = LogManager.getLogger(UserResource.class);

    private final NovelService novelService;
    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovels() {
        List<Novel> novelList = novelService.getNovels();
        if(novelList == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/get/{name}")
    @ResponseBody
    public ResponseEntity<Novel> getNovelByName(@PathVariable String name) {
        Novel novel = novelService.findByName(name);
        if(novel == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<Novel>(novel, HttpStatus.OK);
    }
}
