package com.socialmedia.loginandregistration.mapping;

import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.RegisterRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapping {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private RegisterRequest registerRequest;
    private User userEntity;

    public UserMapping(RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
        this.registerRequest.setPassword(passwordEncoder.encode(this.registerRequest.getPassword()));
    }

    public UserMapping(User userEntity) {
        this.userEntity = userEntity;
    }

    public User getUserEntity() {
        this.registerToEntity();
        return userEntity;
    }

    private void registerToEntity(){
        this.userEntity = new User();
        this.userEntity.setUsername(this.registerRequest.getUsername());
        this.userEntity.setPassword(this.registerRequest.getPassword());
        this.userEntity.setEmail(this.registerRequest.getEmail());
        this.userEntity.setFirstname("");
        this.userEntity.setLastname("");
        this.userEntity.setStatus("UnActive");
    }
}
