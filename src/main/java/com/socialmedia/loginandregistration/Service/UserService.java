package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.model.Entity.Role;
import com.socialmedia.loginandregistration.model.Entity.User;
import org.springframework.stereotype.Service;


import java.util.List;


public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
    Boolean existsByEmail(String email);

}
