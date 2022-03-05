package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.model.Entity.Role;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.model.payload.request.InfoUserRequest;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


public interface UserService {
    User saveUser(User user,String roleName);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    User findByUsername(String username);
    User updateUserInfo(User user, InfoUserRequest userInfo);
    User updateUserPassword(User user, String password);
    Optional<User> deleteUser(String username);
}
