package com.socialmedia.loginandregistration.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.loginandregistration.Service.UserService;
import com.socialmedia.loginandregistration.mapping.UserMapping;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.RegisterRequest;
import com.socialmedia.loginandregistration.security.DTO.AppUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthentiactionController {
    private static final Logger LOGGER = LogManager.getLogger(AuthentiactionController.class);

    private final UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity  addUser(@RequestBody @Valid RegisterRequest user) {
        if (user != null) {
            LOGGER.info("Inside addIssuer, adding: " + user.toString());
        } else {
            LOGGER.info("Inside addIssuer...");
        }
        if(userService.existsByEmail(user.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email already taken");
        }
        try{

            UserMapping userMapping = new UserMapping(user);
            User user1 = userMapping.getUserEntity();

            userService.saveUser(user1);
            userService.addRoleToUser(user.getEmail(),"ADMIN");
            return ResponseEntity.ok("Account has been created");

        }catch(ResponseStatusException ex){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Can't create your account");
        }
    }


    @GetMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {

                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();

                AppUserDetail user = AppUserDetail.build(userService.getUser(email));
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ 10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception ex){

                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",ex.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            }
        }
        else
        {
            throw new RuntimeException("refresh token is missing");
        }
    }

}
