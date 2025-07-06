package com.workable.movierama.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;


/** * Configuration for JWT validation.
 * This class sets up a JwtDecoder that uses the JWK Set URI to validate JWT tokens.
 * It does not perform issuer validation, only checks for expiration and not before claims.
 */
@Configuration
public class JwtValidationConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = issuerUri + "/protocol/openid-connect/certs";
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        OAuth2TokenValidator<Jwt> withTimestamp = JwtValidators.createDefault();
        decoder.setJwtValidator(withTimestamp);
        return decoder;
    }

}
