package com.socialmedia.loginandregistration.controller;

import com.socialmedia.loginandregistration.Service.RoleService;
import com.socialmedia.loginandregistration.Service.UserService;
import com.socialmedia.loginandregistration.mapping.UserMapping;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.RegisterAdminRequest;
import com.socialmedia.loginandregistration.model.payload.request.RoleToUserRequest;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.HttpMessageNotReadableException;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.MethodArgumentNotValidException;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.RecordNotFoundException;
import com.socialmedia.loginandregistration.model.payload.response.ErrorResponseMap;
import com.socialmedia.loginandregistration.model.payload.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserResources {
    private static final Logger LOGGER = LogManager.getLogger(AdminResource.class);

    private final UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;


    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        if(userList == null) {
            throw new RecordNotFoundException("No User existing " );
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

}