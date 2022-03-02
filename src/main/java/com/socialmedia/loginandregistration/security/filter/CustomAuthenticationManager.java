package com.socialmedia.loginandregistration.security.filter;

import com.socialmedia.loginandregistration.security.DTO.AppUserDetail;
import com.socialmedia.loginandregistration.security.Service.AppUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class CustomAuthenticationManager  implements AuthenticationManager {
    @Autowired
    private AppUserDetailService appUserDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        AppUserDetail user =(AppUserDetail) appUserDetailService.loadUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("1000");
        }
        if (!(passwordEncoder.matches(password,user.getPassword()))) {
            throw new BadCredentialsException("1000");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("1001");
        }
        return new UsernamePasswordAuthenticationToken(username,user.getPassword(),user.getAuthorities());
    }
}
