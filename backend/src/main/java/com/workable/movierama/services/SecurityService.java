package com.workable.movierama.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((Jwt) authentication.getPrincipal()).getClaim("preferred_username");
    }
}
