package com.workable.movierama.configuration.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.util.annotation.NonNull;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class KcAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt token) {

        return new JwtAuthenticationToken(
                token,
                Stream.concat(
                                new JwtGrantedAuthoritiesConverter().convert(token).stream(),
                                getUserRoles(token).stream())
                        .collect(toSet()));
    }

    private Collection<? extends GrantedAuthority> getUserRoles(Jwt jwt) {

        var resourceAccess = new HashMap<>(jwt.getClaim("resource_access"));
        var accountMap = (Map<String, List<String>>) resourceAccess.get("movierama-client");
        var roles = (accountMap != null && accountMap.get("roles") != null) ? accountMap.get("roles") : List.of("viewer");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(toSet());
    }

}
