package com.socialmedia.loginandregistration.Service.Impl;

import com.socialmedia.loginandregistration.Service.RoleService;
import com.socialmedia.loginandregistration.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class RoleServiceImpl implements RoleService {

    final RoleRepository roleRepository;

    @Override
    public Boolean existsByRoleName(String RoleName) {
        return roleRepository.existsByName(RoleName);
    }
}
