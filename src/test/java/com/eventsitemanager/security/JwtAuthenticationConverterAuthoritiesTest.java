package com.eventsitemanager.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * Ensures OAuth2 resource-server {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken}
 * receives {@link org.springframework.security.core.GrantedAuthority} from the same mapping as service/API JWTs
 * ({@code auth}, {@code authorities}, {@code roles}, plus {@code jwtadmin} fallback).
 */
class JwtAuthenticationConverterAuthoritiesTest {

    private static JwtAuthenticationConverter converterUsingMapper() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(JwtClaimAuthoritiesMapper::mapAuthorities);
        return converter;
    }

    @Test
    void bearerJwtIncludesAuthoritiesFromAuthoritiesClaim() {
        Jwt jwt = Jwt
            .withTokenValue("token")
            .header("alg", "HS256")
            .subject("jwtadmin")
            .claim("authorities", List.of("ROLE_ADMIN", "ROLE_USER"))
            .build();

        var auth = converterUsingMapper().convert(jwt);
        assertThat(auth.getAuthorities()).extracting("authority").containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void bearerJwtMapsAuthClaimString() {
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "HS256").subject("jwtadmin").claim("auth", "ROLE_ADMIN ROLE_USER").build();

        var auth = converterUsingMapper().convert(jwt);
        assertThat(auth.getAuthorities()).extracting("authority").contains("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void jwtadminWithoutClaimsGetsFallbackRoleAdmin() {
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "HS256").subject("jwtadmin").build();

        var auth = converterUsingMapper().convert(jwt);
        assertThat(auth.getAuthorities()).extracting("authority").contains("ROLE_ADMIN");
    }

    @Test
    void nonJwtadminWithoutClaimsHasNoGrantedAuthorities() {
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "HS256").subject("user-123").build();

        var auth = converterUsingMapper().convert(jwt);
        assertThat(auth.getAuthorities()).isEmpty();
    }
}
