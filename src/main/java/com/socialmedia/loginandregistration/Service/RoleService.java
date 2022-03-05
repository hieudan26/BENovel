package com.socialmedia.loginandregistration.Service;

import com.socialmedia.loginandregistration.Service.Impl.RoleServiceImpl;

public interface RoleService {
    Boolean existsByRoleName(String RoleName);
}
