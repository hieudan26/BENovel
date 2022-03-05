package com.socialmedia.loginandregistration.controller;

import com.socialmedia.loginandregistration.Service.RoleService;
import com.socialmedia.loginandregistration.Service.UserService;
import com.socialmedia.loginandregistration.mapping.UserMapping;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.InfoUserRequest;
import com.socialmedia.loginandregistration.model.payload.request.RegisterAdminRequest;
import com.socialmedia.loginandregistration.model.payload.request.RegisterRequest;
import com.socialmedia.loginandregistration.model.payload.request.RoleToUserRequest;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.HttpMessageNotReadableException;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.MethodArgumentNotValidException;
import com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse.RecordNotFoundException;
import com.socialmedia.loginandregistration.model.payload.response.ErrorResponseMap;
import com.socialmedia.loginandregistration.model.payload.response.SuccessResponse;
import com.socialmedia.loginandregistration.security.DTO.AppUserDetail;
import com.socialmedia.loginandregistration.security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserResources {
    private static final Logger LOGGER = LogManager.getLogger(AdminResource.class);

    private final UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @PutMapping("/info")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  updateInfo(@RequestBody @Valid InfoUserRequest userInfo, BindingResult errors, HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));
            user = userService.updateInfoUser(user,userInfo);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Update info successful");
            response.setSuccess(true);
            response.getData().put("userInfo",user);

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  deleteUser(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            userService.deleteUser(username);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("delete successful");
            response.setSuccess(true);
            response.getData().put("username",username);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    private ResponseEntity SendErrorValid(String field, String message){
        ErrorResponseMap errorResponseMap = new ErrorResponseMap();
        Map<String,String> temp =new HashMap<>();
        errorResponseMap.setMessage("Field already taken");
        temp.put(field,message+" has already used");
        errorResponseMap.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseMap.setDetails(temp);
        return ResponseEntity
                .badRequest()
                .body(errorResponseMap);
    }
}