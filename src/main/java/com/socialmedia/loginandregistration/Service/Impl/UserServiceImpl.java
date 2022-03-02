package com.socialmedia.loginandregistration.Service.Impl;

import com.socialmedia.loginandregistration.Service.UserService;

import com.socialmedia.loginandregistration.model.Entity.Role;
import com.socialmedia.loginandregistration.model.Entity.User;
import com.socialmedia.loginandregistration.repository.RoleRepository;
import com.socialmedia.loginandregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    @Override
    public User saveUser(User user) {

        log.info("Saving user {} to database",user.getEmail());
        user.setId(new ObjectId());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving Role {} to database",role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding Role {} to user {}", roleName, email);
        Optional<Role> role = roleRepository.findByName(roleName);
        Optional<User> user = userRepository.findByEmail(email);

        if(user.get().getRoles() == null){
            Set<Role> RoleSet = new HashSet<>();
            RoleSet.add(role.get());
            user.get().setRoles(RoleSet);
        }
        else{
            user.get().getRoles().add(role.get());
        }
        userRepository.save(user.get());
    }

    @Override
    public User getUser(String email) {
        log.info("Fetching user {}",email);
        return userRepository.findByUsername(email).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users ");
        return userRepository.findAll();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
