package com.socialmedia.loginandregistration.controller;

import com.socialmedia.loginandregistration.Service.UserService;
import com.socialmedia.loginandregistration.mapping.UserMapping;
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
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class UserResource {
    private static final Logger LOGGER = LogManager.getLogger(UserResource.class);

    private final UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;


    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        if(userList == null) {
            throw new RecordNotFoundException("No User existing " );
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PostMapping("user/save")
    @ResponseBody
    public ResponseEntity<User> saveUser(@RequestBody @Valid RegisterRequest user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(new UserMapping(user).getUserEntity()));
    }

    @PostMapping("role/addtouser")
    @ResponseBody
    public ResponseEntity<?> addRoleToUser(@RequestBody @Valid RoleToUserRequest roleForm) {
        userService.addRoleToUser(roleForm.getEmail(),roleForm.getRoleName());
        return ResponseEntity.ok().build();
    }
}