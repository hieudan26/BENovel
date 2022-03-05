package com.socialmedia.loginandregistration.mapping;

import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.InfoUserRequest;
import com.socialmedia.loginandregistration.model.payload.request.RegisterAdminRequest;
import com.socialmedia.loginandregistration.model.payload.request.RegisterRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapping {

    public static User registerToEntity(RegisterRequest registerRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return new User(registerRequest.getUsername(),registerRequest.getEmail(),registerRequest.getPassword());
    }

    public static User registerToEntity(RegisterAdminRequest registerRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return new User(registerRequest.getUsername(),registerRequest.getEmail(),registerRequest.getPassword());
    }

    public static User UpdateUserInfoByUser(User user, InfoUserRequest userInfo) {
        user.setBirthdate(userInfo.getBirthdate());
        user.setTenhienthi(userInfo.getTenhienthi());
        user.setImage(userInfo.getImage());
        return  user;
    }
}
